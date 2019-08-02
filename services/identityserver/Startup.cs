// Copyright (c) Brock Allen & Dominick Baier. All rights reserved.
// Licensed under the Apache License, Version 2.0. See LICENSE in the project root for license information.

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.DataProtection;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using IdentityServer.Data;
using IdentityServer.Models;
using IdentityServer4.EntityFramework.DbContexts;
using IdentityServer4;
using Microsoft.AspNetCore.Authentication;
using System.Security.Claims;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Authentication.OAuth;
using System.Net.Http;
using System.Net.Http.Headers;
using Newtonsoft.Json.Linq;
using System.Security.Cryptography.X509Certificates;
using System.IO;
using IdentityModel;

namespace IdentityServer
{

    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            var migrationsAssembly = typeof(Startup).Assembly.GetName().Name;
            services.AddDbContext<ApplicationDbContext>(options =>
                options.UseSqlServer(Configuration.GetConnectionString("DefaultConnection")));

            services.AddIdentity<ApplicationUser, IdentityRole>()
                .AddEntityFrameworkStores<ApplicationDbContext>()
                .AddDefaultTokenProviders();

            services.AddMvc();

            services.AddIdentityServer()
                //.AddDeveloperSigningCredential()
                .AddSigningCredential(LoadCertificate())
                .AddInMemoryPersistedGrants()
                .AddInMemoryIdentityResources(Config.GetIdentityResource())
                .AddInMemoryApiResources(Config.GetApiResources())
                .AddInMemoryClients(Config.GetClients())
                .AddOperationalStore(options =>
                {
                    options.ConfigureDbContext = builder =>
                        builder.UseSqlServer(Configuration.GetConnectionString("DefaultConnection"), 
                        sql => sql.MigrationsAssembly(migrationsAssembly));

                    options.EnableTokenCleanup = true;
                    options.TokenCleanupInterval = 30;
                })
                .AddAspNetIdentity<ApplicationUser>()
                .AddProfileService<IdentityProfileService>();

            //Add external logins
            //description how to set-up additional common other providers can be found here:
            //https://docs.microsoft.com/en-us/aspnet/core/security/authentication/social/ 
            services.AddAuthentication()
                .AddGoogle("Google", options =>
                {
                    //1. Login at https://console.developers.google.com/
                    //2. Create a new project
                    //3. Add Google+ API
                    //4. Create credentials "OAuth client ID" (menu on the left)
                    //  - Type: Web-Application
                    //  - Name: <enter a name>
                    //  - Authorized JavaScript origins can be empty
                    //  - Authorized redirect URIs: http://localhost:5000/signin-google
                    //  - Create credentials
                    //5. Click on listed credentials and copy client id / client secret to this configuration
                    //6. Log-In via Google button on identityserver and create an account

                    //SignInScheme taken from https://github.com/IdentityServer/IdentityServer4/issues/822
                    //To debug login add a breakpoint to AccountController.ExternalLoginCallback()
                    //If correct configured _signInManager.GetExternalLoginInfoAsync(); returns ExternalLoginInfo
                    options.SignInScheme = "Identity.External";
                    options.ClientId = "<client-id>";
                    options.ClientSecret = "<client-secret>";
                });

            //Add OAuth external login
            //Configuration taken from here:
            //https://www.jerriepelser.com/blog/authenticate-oauth-aspnet-core-2/
            services.AddAuthentication()
                .AddOAuth("Github", options =>
                {
                    //1. Login at https://github.com/settings/developers
                    //2. Create a "New OAuth App" / "Register a new application"
                    //3. Enter the following parameters:
                    //  - Application name: <enter a name>
                    //  - Homepage URL: http://localhost:5000
                    //  - Authorization callback URL: http://localhost:5000/signin-github
                    //  - Click on "Register application"
                    //4. Copy client id / client secret to this configuration
                    //5. Log-In via Github button on identityserver and create an account

                    options.ClientId = "<client-id>";
                    options.ClientSecret = "<client-secret>";
                    options.CallbackPath = new PathString("/signin-github");

                    options.AuthorizationEndpoint = "https://github.com/login/oauth/authorize";
                    options.TokenEndpoint = "https://github.com/login/oauth/access_token";
                    options.UserInformationEndpoint = "https://api.github.com/user";

                    options.ClaimActions.MapJsonKey(ClaimTypes.NameIdentifier, "id");
                    options.ClaimActions.MapJsonKey(ClaimTypes.Name, "name");
                    options.ClaimActions.MapJsonKey("urn:github:login", "login");
                    options.ClaimActions.MapJsonKey("urn:github:url", "html_url");
                    options.ClaimActions.MapJsonKey("urn:github:avatar", "avatar_url");

                    options.Events = new OAuthEvents
                    {
                        OnCreatingTicket = async context =>
                        {
                            var request = new HttpRequestMessage(HttpMethod.Get, context.Options.UserInformationEndpoint);
                            request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                            request.Headers.Authorization = new AuthenticationHeaderValue("Bearer", context.AccessToken);

                            var response = await context.Backchannel.SendAsync(request, HttpCompletionOption.ResponseHeadersRead, context.HttpContext.RequestAborted);
                            response.EnsureSuccessStatusCode();

                            var user = JObject.Parse(await response.Content.ReadAsStringAsync());

                            context.RunClaimActions(user);
                        }
                    };
                });

                services.AddDataProtection().SetApplicationName("Identity Server");
                services.AddAntiforgery();

                //TODO
                //https://docs.microsoft.com/de-de/aspnet/core/security/enforcing-ssl
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            InitializeDatabase(app);
            InitializeUsers(app);

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseBrowserLink();
                app.UseDatabaseErrorPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
            }

            app.UseStaticFiles();
            
            app.UseIdentityServer();

            app.UseMvc(routes =>
            {
                routes.MapRoute(
                    name: "default",
                    template: "{controller=Home}/{action=Index}/{id?}");
            });
        }

        private async void InitializeUsers(IApplicationBuilder app)
        {
            using(var serviceScope = app.ApplicationServices.GetRequiredService<IServiceScopeFactory>().CreateScope())
            {
                var dbContext = serviceScope.ServiceProvider.GetRequiredService<ApplicationDbContext>();
                if(dbContext.Users.Any())
                {
                    return;
                }
                var userManager = serviceScope.ServiceProvider.GetRequiredService<UserManager<ApplicationUser>>();

                foreach(var user in Config.GetUsers())
                {
                    var dbUser = new ApplicationUser { Id = user.SubjectId, UserName = user.Username };
                    var createUserResult = await userManager.CreateAsync(dbUser, user.Password);
                    if(!createUserResult.Succeeded)
                    {
                        throw new ApplicationException($"Unexpected error occurred created user with username '{user.Username}'.");
                    }
                    foreach(Claim claim in user.Claims)
                    {
                        var claimResult = await userManager.AddClaimAsync(dbUser, claim);
                        if (!claimResult.Succeeded)
                        {
                            throw new ApplicationException($"Unexpected error occurred setting claim for user with username '{user.Username}'.");
                        }
                    }
                }
            }
        }

        private void InitializeDatabase(IApplicationBuilder app)
        {
            using (var serviceScope = app.ApplicationServices.GetService<IServiceScopeFactory>().CreateScope())
            {
                //Generate migration with the following command, delete old before generating:
                //dotnet ef migrations add CreateIdentitySchema -o "Data/Migrations/AspNetCoreIdentity" --context "ApplicationDbContext"
                serviceScope.ServiceProvider.GetRequiredService<ApplicationDbContext>().Database.Migrate();
                //Generate migration with the following command, delete old before generating:
                //dotnet ef migrations add PersistedGrantDbMigration -o "Data/Migrations/PersistedGrantDb" --context "PersistedGrantDbContext"
                serviceScope.ServiceProvider.GetRequiredService<PersistedGrantDbContext>().Database.Migrate();
            }
        }

        private X509Certificate2 LoadCertificate()
        {
            return new X509Certificate2(
                Path.Combine(AppDomain.CurrentDomain.BaseDirectory,@"certificate.pfx"), "secret");
        }
    }
}

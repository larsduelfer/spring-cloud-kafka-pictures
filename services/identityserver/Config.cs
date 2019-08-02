// Copyright (c) Brock Allen & Dominick Baier. All rights reserved.
// Licensed under the Apache License, Version 2.0. See LICENSE in the project root for license information.

using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using IdentityModel;
using IdentityServer.Models;
using IdentityServer4;
using IdentityServer4.Models;
using IdentityServer4.Test;
using static IdentityServer4.IdentityServerConstants;

namespace IdentityServer
{

    public class Config
    {

        public static IEnumerable<IdentityResource> GetIdentityResource()
        {
            return new List<IdentityResource>
            {
                new IdentityResources.OpenId(),
                new IdentityResources.Profile(),
                new IdentityResources.Email()
            };
        }

        public static IEnumerable<ApiResource> GetApiResources()
        {
            return new List<ApiResource>
            {
                new ApiResource("web", "Web API"),
                new ApiResource("api", "Technical API")
            };
        }

        public static IEnumerable<Client> GetClients()
        {
            return new List<Client>
            {
				new Client
                {
                    ClientId = "api-localhost",
                    ClientName = "API client on localhost. Only for testing purposes",
                    AllowedGrantTypes = GrantTypes.ResourceOwnerPassword,

                    ClientSecrets =
                    {
                        new Secret("secret".Sha256())
                    },
                    AllowedScopes = { "api" }
                },
                new Client
                {
					ClientId = "web-localhost",
                    ClientName = "Web client on localhost",
                    AllowedGrantTypes = GrantTypes.Implicit,
                    
                    RequireConsent = false,

                    RedirectUris = { "http://localhost:4200/dashboard" },
                    PostLogoutRedirectUris = { "http://localhost:4200/dashboard" },

                    AllowedCorsOrigins = { "http://localhost:4200" },

                    AllowedScopes = new List<string>
                    {
                        IdentityServerConstants.StandardScopes.OpenId,
                        IdentityServerConstants.StandardScopes.Profile,
                        JwtClaimTypes.Email,

                        "web"
                    },
                    AlwaysIncludeUserClaimsInIdToken = true,
                    AllowAccessTokensViaBrowser = true
                },
            };
        }

        public static List<TestUser> GetUsers()
        {
            return new List<TestUser> {};
        }
    }
}

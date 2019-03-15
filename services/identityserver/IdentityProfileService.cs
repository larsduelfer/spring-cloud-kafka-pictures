// Copyright (c) Brock Allen & Dominick Baier. All rights reserved.
// Licensed under the Apache License, Version 2.0. See LICENSE in the project root for license information.

using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using IdentityServer.Models;
using System.Security.Claims;
using IdentityServer4.Services;
using IdentityServer4.Models;
using IdentityModel;
using IdentityServer4;

namespace IdentityServer
{
    public class IdentityProfileService : IProfileService
    {
        private readonly UserManager<ApplicationUser> _userManager;

        public IdentityProfileService(UserManager<ApplicationUser> userManager)
        {
            _userManager = userManager;
        }

        public async Task GetProfileDataAsync(ProfileDataRequestContext context)
        {
            if(context.Caller == "ClaimsProviderAccessToken") {
                return;
            }
            var sub = context.Subject?.FindFirst(JwtClaimTypes.Subject)?.Value;
            var user = await _userManager.FindByIdAsync(sub);
            if (user == null)
            {
                throw new ArgumentException("User not found");
            }

            bool clientEmailAllowed = context.Client.AllowedScopes?.FirstOrDefault(scope => scope == IdentityServerConstants.StandardScopes.Email) != null;
            bool clientProfileAllowed = context.Client.AllowedScopes?.FirstOrDefault(scope => scope == IdentityServerConstants.StandardScopes.Profile) != null;

            if(clientEmailAllowed || clientProfileAllowed)
            {
                var requestedResources = context.RequestedResources;
                bool userRequestedEmail = requestedResources?.IdentityResources?.FirstOrDefault(requestedResource => requestedResource.Name.Equals(JwtClaimTypes.Email)) != null;
                bool userRequestedProfile = requestedResources?.IdentityResources?.FirstOrDefault(requestedResource => requestedResource.Name.Equals(JwtClaimTypes.Profile)) != null;

                var userClaims = await _userManager.GetClaimsAsync(user);
                List<Claim> claims = new List<Claim>();
                if(clientProfileAllowed && userRequestedProfile)
                {
                    var firstName = userClaims?.Where(claim => claim.Type == JwtClaimTypes.GivenName).FirstOrDefault();
                    if(firstName != null)
                    {
                        claims.Add(firstName);
                    }
                    var lastName = userClaims?.Where(claim => claim.Type == JwtClaimTypes.FamilyName).FirstOrDefault();
                    if(lastName != null)
                    {
                        claims.Add(lastName);
                    }
                }
                if(clientEmailAllowed && userRequestedEmail)
                {
                    claims.Add(new Claim(JwtClaimTypes.Email, user.UserName));
                }

                context.IssuedClaims = claims;
            }
        }

        public async Task IsActiveAsync(IsActiveContext context)
        {
            var sub = context.Subject?.FindFirst(JwtClaimTypes.Subject)?.Value;
            if(sub == null)
            {
                context.IsActive = false;
            }
            else
            {
                var user = await _userManager.FindByIdAsync(sub);
                context.IsActive = user != null;
            }
        }
    }
}
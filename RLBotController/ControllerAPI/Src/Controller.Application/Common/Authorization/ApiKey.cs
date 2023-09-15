using System.Collections.Generic;
using System.Security.Claims;
using AspNetCore.Authentication.ApiKey;

namespace Controller.Application.Common.Authorization;

public sealed record ApiKey(string Key, string OwnerName, IReadOnlyCollection<Claim> Claims) : IApiKey;
using System;
using System.Linq;
using System.Security.Claims;
using Microsoft.AspNetCore.Http;

namespace Controller.Application.Common.Services.ClientContext;

public class ClientContext : IClientContext
{
    private readonly IHttpContextAccessor _httpContextAccessor;

    public ClientContext(IHttpContextAccessor httpContextAccessor)
    {
        _httpContextAccessor = httpContextAccessor;
    }

    public long ClientId
    {
        get
        {
            var identifier = _httpContextAccessor.HttpContext?.User.Claims.FirstOrDefault(c => c.Type == ClaimTypes.NameIdentifier);

            if (identifier == null)
            {
                throw new InvalidOperationException("Client not authenticated");
            }

            return long.Parse(identifier.Value);
        }
    }
}
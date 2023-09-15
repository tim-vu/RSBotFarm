using System.Security.Claims;
using System.Threading.Tasks;
using AspNetCore.Authentication.ApiKey;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Common.Authorization;

public class ApiKeyProvider : IApiKeyProvider
{
    private readonly RSBControllerContext _context;

    public ApiKeyProvider(RSBControllerContext context)
    {
        _context = context;
    }

    public async Task<IApiKey> ProvideAsync(string key)
    {
        var client = await _context.Clients.SingleOrDefaultAsync(c => c.BotId == key);

        if (client == null)
        {
            return null!;
        }
        
        var claims = new[]
        {
            new Claim(ClaimTypes.NameIdentifier, client.Id.ToString()),
        };
        
        return new ApiKey(client.BotId, client.Id.ToString(), claims);
    }
}
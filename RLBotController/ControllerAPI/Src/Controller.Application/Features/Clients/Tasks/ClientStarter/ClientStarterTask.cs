using System.Linq;
using System.Threading.Tasks;
using Controller.Application.Common.ScheduledTask;
using Controller.Application.Common.Services.Clock;
using Controller.Application.Domain.Enums;
using Controller.Application.Features.Clients.Services.ClientManager;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace Controller.Application.Features.Clients.Tasks.ClientStarter;

[ScheduledTask(3)]
public class ClientStarterTask : IScheduledTask
{
    private readonly RSBControllerContext _context;
    private readonly IClientManager _clientManager;
    private readonly IClock _clock;
    private readonly ILogger<ClientStarterTask> _logger;

    public ClientStarterTask(RSBControllerContext context, IClientManager clientManager, IClock clock, ILogger<ClientStarterTask> logger)
    {
        _context = context;
        _clientManager = clientManager;
        _clock = clock;
        _logger = logger;
    }

    public async Task DoExecute()
    {
        var client = await _context.Clients
            .Where(c => c.Status == ClientStatus.Created)
            .FirstOrDefaultAsync();

        if (client == null)
        {
            return; 
        }

        var script = await _context.Scripts.FindAsync(client.ActiveSession.ScriptId);
        var account = await _context.Accounts.FindAsync(client.ActiveSession.AccountId);

        if (script == null || account == null)
        {
            return;
        }

        _logger.LogInformation("Starting client with BotId {BotId}", client.BotId);
        
        client.Start(_clock.UtcNow);
        
        var startedClient = await _clientManager.LaunchClient(client, script, account);

        client.VncPort = startedClient.VncPort;
        client.WebsocketPort = startedClient.WebsocketPort;
        
        await _context.SaveChangesAsync();
    }
}
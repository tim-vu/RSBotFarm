using System.Linq;
using System.Threading.Tasks;
using Controller.Application.Common.ScheduledTask;
using Controller.Application.Common.Services.Clock;
using Controller.Application.Domain.Enums;
using Controller.Application.Features.Clients.Services.ClientManager;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace Controller.Application.Features.Clients.Tasks.ClientStoppedMarker;

[ScheduledTask(1)]
public class ClientStoppedMarkerTask : IScheduledTask
{
    private readonly RSBControllerContext _context;
    private readonly IClientManager _clientManager;
    private readonly IClock _clock;
    private readonly ILogger<ClientStoppedMarkerTask> _logger;
    
    public ClientStoppedMarkerTask(RSBControllerContext context, IClientManager clientManager, IClock clock, ILogger<ClientStoppedMarkerTask> logger)
    {
        _context = context;
        _clientManager = clientManager;
        _clock = clock;
        _logger = logger;
    }

    public async Task DoExecute()
    {
        var clients = await _context.Clients
            .Where(c => c.Status != ClientStatus.Created)
            .Where(c => c.Status != ClientStatus.Stopped)
            .ToListAsync();

        var runningClientBotIds = (await _clientManager.GetClients())
            .Select(c => c.BotId)
            .ToHashSet();

        var stoppedClient = clients.FirstOrDefault(c => !runningClientBotIds.Contains(c.BotId));

        if (stoppedClient == null)
        {
            return;
        }

        _logger.LogInformation("Marking client with BotId {BotId} as stopped", stoppedClient.BotId);
        stoppedClient.Stop(_clock.UtcNow);
        await _context.SaveChangesAsync();
    }
}
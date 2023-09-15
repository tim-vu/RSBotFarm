using System;
using System.Linq;
using System.Threading.Tasks;
using Controller.Application.Common.ScheduledTask;
using Controller.Application.Common.Services.Clock;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.Events;
using Controller.Application.Domain.ValueObjects;
using Controller.Application.Features.Allocations.ResourceAllocator;
using Controller.Application.Features.Clients.Tasks.ClientDirector.ClientListSynchronizer;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace Controller.Application.Features.Clients.Tasks.ClientDirector;

[ScheduledTask(10)]
public class ClientDirectorTask : IScheduledTask
{
    private readonly RSBControllerContext _context;
    private readonly IResourceAllocator _resourceAllocator;
    private readonly IClientListSynchronizer _clientListSynchronizer;
    private readonly IClock _clock;
    private readonly ILogger<ClientDirectorTask> _logger;
    
    public ClientDirectorTask(
        RSBControllerContext context,
        IResourceAllocator resourceAllocator, 
        IClientListSynchronizer clientListSynchronizer,
        IClock clock,
        ILogger<ClientDirectorTask> logger)
    {
        _context = context;
        _resourceAllocator = resourceAllocator;
        _clientListSynchronizer = clientListSynchronizer;
        _logger = logger;
        _clock = clock;
    }
    
    public async Task DoExecute()
    {
        var controlState = await _context.ControlStates.SingleAsync();

        if (controlState.Status == FarmStatus.Paused)
        {
            return;
        }
        
        var accounts = await _context.Accounts
            .Where(a => a.AccountStatus == AccountStatus.Valid)
            .ToListAsync();

        var scripts = await _context.Scripts
            .ToListAsync();
        
        var clients  = await _clientListSynchronizer.GetAvailableClients();

        if (controlState.Status == FarmStatus.Stopped)
        {
            foreach (var client in clients)
            {
                client.RequestGracefulStop();
            }

            await _context.SaveChangesAsync();
            return;
        }

        var allocations = _resourceAllocator.AllocateResources(clients, scripts, accounts);

        foreach (var allocation in allocations)
        {
            if (allocation.RequiresNewClient)
            {
                var botId = Guid.NewGuid().ToString();
                _logger.LogInformation("Creating new client with BotId {BotId}", botId);
                //TODO: Use factory
                var client = new Client(botId, new ActiveSession(allocation.Script.Id, allocation.Account.Id, _clock.UtcNow));
                client.AddEvent(new ClientStatusChangedEvent(client));
                _context.Clients.Add(client);
            }
        }
        
        foreach (var client in clients.ExceptBy(allocations.Select(a => a.Client?.Id), c => c.Id))
        {
            _logger.LogInformation("Requesting graceful stop for client with BotId {BotId}", client.BotId);
            client.RequestGracefulStop();
        }

        await _context.SaveChangesAsync();
    }
}
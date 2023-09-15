using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Enums;
using Controller.Application.Features.Clients.Services.ClientManager;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace Controller.Application.Features.Clients.Tasks.ClientDirector.ClientListSynchronizer;

public class ClientListSynchronizer : IClientListSynchronizer
{
    private readonly RSBControllerContext _context;
    private readonly IClientManager _clientManager;
    private readonly ILogger<ClientListSynchronizer> _logger;

    public ClientListSynchronizer(RSBControllerContext context, IClientManager clientManager,  ILogger<ClientListSynchronizer> logger)
    {
        _context = context;
        _clientManager = clientManager;
        _logger = logger;
    }
    
    public async Task<List<Client>> GetAvailableClients()
    {
        var runningClients = await _clientManager.GetClients();
        var clients = await _context.Clients
            .ToListAsync();
        
        var botIdToClient = clients.ToDictionary(c => c.BotId);
        
        var result = new HashSet<Client>();
        
        foreach (var runningClient in runningClients)
        {
            if (!botIdToClient.TryGetValue(runningClient.BotId, out var client))
            {
                _logger.LogWarning("Unknown client with BotId {BotId} running", runningClient.BotId); 
                continue;
            }

            if (client.Status is ClientStatus.StopRequested or ClientStatus.Stopped)
            {
                continue;
            }

            result.Add(client);
        }

        foreach (var createdClient in clients.Where(c => c.Status == ClientStatus.Created))
        {
            result.Add(createdClient);
        }

        return result.ToList();
    }
}
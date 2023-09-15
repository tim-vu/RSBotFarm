using System.Collections.Generic;
using System.Threading.Tasks;
using Controller.Application.Domain.Entities;

namespace Controller.Application.Features.Clients.Services.ClientManager;

public interface IClientManager
{
    Task<List<RunningClientDto>> GetClients();

    Task<StartedClientDto> LaunchClient(Client client, Script script, Account account);
    
    Task StopClient(string processId);
}
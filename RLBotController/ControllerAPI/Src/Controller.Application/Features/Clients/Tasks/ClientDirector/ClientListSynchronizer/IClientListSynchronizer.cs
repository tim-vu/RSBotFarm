using System.Collections.Generic;
using System.Threading.Tasks;
using Controller.Application.Domain.Entities;

namespace Controller.Application.Features.Clients.Tasks.ClientDirector.ClientListSynchronizer;

public interface IClientListSynchronizer
{
    Task<List<Client>> GetAvailableClients();
}
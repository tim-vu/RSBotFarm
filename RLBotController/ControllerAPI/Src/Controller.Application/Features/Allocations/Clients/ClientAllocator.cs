using System.Collections.Generic;
using System.Linq;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Allocations.Models;

namespace Controller.Application.Features.Allocations.Clients;

public class ClientAllocator : IClientAllocator
{
    public List<ResourceAllocationDto> AllocateClients(
        List<Client> clients,
        List<ResourceAllocationDto> allocations)
    {
        var result = new List<ResourceAllocationDto>();

        foreach (var accountAllocation in allocations)
        {
            var existingClient = clients
                .FirstOrDefault(c => accountAllocation.MatchesActiveSession(c.ActiveSession!));

            if (existingClient != null)
            {
                result.Add(new ResourceAllocationDto
                {
                    Client = existingClient,
                    Script = accountAllocation.Script,
                    Account = accountAllocation.Account
                });
                continue;
            }

            result.Add(new ResourceAllocationDto
            {
                Client = null,
                Script = accountAllocation.Script,
                Account = accountAllocation.Account
            });
        }

        return result;
    }
}
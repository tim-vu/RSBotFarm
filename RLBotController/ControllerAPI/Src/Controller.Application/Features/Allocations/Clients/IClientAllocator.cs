using System.Collections.Generic;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Allocations.Models;

namespace Controller.Application.Features.Allocations.Clients;

public interface IClientAllocator
{
    List<ResourceAllocationDto> AllocateClients(List<Client> clients, List<ResourceAllocationDto> allocations);
}
using System.Collections.Generic;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Allocations.Models;

namespace Controller.Application.Features.Allocations.ResourceAllocator;

public interface IResourceAllocator
{
    List<ResourceAllocationDto> AllocateResources(
        List<Client> clients,
        List<Script> scripts,
        List<Account> accounts
    );
}
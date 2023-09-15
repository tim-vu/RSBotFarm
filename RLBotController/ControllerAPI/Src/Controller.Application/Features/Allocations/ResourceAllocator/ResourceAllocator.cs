using System.Collections.Generic;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Allocations.Accounts;
using Controller.Application.Features.Allocations.Clients;
using Controller.Application.Features.Allocations.Models;

namespace Controller.Application.Features.Allocations.ResourceAllocator;

public class ResourceAllocator : IResourceAllocator
{
    private readonly IAccountAllocator _accountAllocator;
    private readonly IClientAllocator _clientAllocator;

    public ResourceAllocator(IAccountAllocator accountAllocator, IClientAllocator clientAllocator)
    {
        _accountAllocator = accountAllocator;
        _clientAllocator = clientAllocator;
    }

    public List<ResourceAllocationDto> AllocateResources(List<Client> clients, List<Script> scripts, List<Account> accounts)
    {
        var allocations = _accountAllocator.AllocateAccounts(scripts, accounts);
        return _clientAllocator.AllocateClients(clients, allocations);
    }
}
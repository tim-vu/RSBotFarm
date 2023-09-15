using System.Collections.Generic;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Allocations.Models;

namespace Controller.Application.Features.Allocations.Accounts;

public interface IAccountAllocator
{
    List<ResourceAllocationDto> AllocateAccounts(List<Script> scripts, List<Account> accounts);
}
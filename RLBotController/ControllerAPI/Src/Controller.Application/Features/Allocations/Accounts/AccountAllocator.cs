using System.Collections.Generic;
using System.Linq;
using Controller.Application.Domain.Constants;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Allocations.Models;

namespace Controller.Application.Features.Allocations.Accounts;

public class AccountAllocator : IAccountAllocator
{
    public List<ResourceAllocationDto> AllocateAccounts(List<Script> scripts, List<Account> accounts)
    {
        var mules = accounts
            .Where(a => a is MuleAccount)
            .OrderBy(a => a.Id)
            .ToList();

        var workers = accounts
            .Where(a => a is WorkerAccount)
            .OrderBy(a => a.Id)
            .ToList();

        scripts = scripts
            .OrderBy(s => s.Priority)
            .ToList();

        var allocatedAccountIds = new HashSet<long>();
        var allocations = new List<ResourceAllocationDto>();
        
        foreach (var script in scripts)
        {
            var candidates = (script.Id == ScriptConstants.MuleScriptId ? mules : workers)
                .Where(a => a.ScriptAssigment == null || a.ScriptAssigment.ScriptId == script.Id)
                .Where(a => !allocatedAccountIds.Contains(a.Id))
                .Take(script.Instances)
                .ToList();

            foreach (var candidate in candidates)
            {
                allocatedAccountIds.Add(candidate.Id);
            }

            allocations.AddRange(candidates.Select(a => new ResourceAllocationDto
            {
                Script = script,
                Account = a
            }));
        }

        return allocations;
    }
}
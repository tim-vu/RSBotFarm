using Controller.Application.Domain.Entities;
using Controller.Application.Domain.ValueObjects;

namespace Controller.Application.Features.Allocations.Models;

public class ResourceAllocationDto
{
    public bool RequiresNewClient => Client == null;

    public bool MatchesActiveSession(ActiveSession activeSession)
    {
        return activeSession.ScriptId == Script.Id && activeSession.AccountId == Account.Id;
    }
    
    public Client? Client { get; init; }
    
    public required Script Script { get; init; }
    
    public required Account Account { get; init; }
}
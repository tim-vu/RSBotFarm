using System;
using System.Collections.Generic;
using System.Linq;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.ValueObjects;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public class ActiveMule : Entity<long>
{
    public long ClientId { get; private set; }
    
    public Position Position { get; private set; }
    
    public int World { get; private set; }

    public int Gold { get; private set; }
    
    public void Deactivate()
    {
        Active = false;
    }
    
    public bool Active { get; private set; } = true;

    public DateTimeOffset LastHeartbeat { get; private set; }
    
    
    public IReadOnlyCollection<MulingRequest> MulingRequests => _mulingRequests.AsReadOnly();

    public void CompleteMulingRequest(long mulingRequestId)
    {
        var request = _mulingRequests.SingleOrDefault(r => r.Id == mulingRequestId);

        if (request == null)
        {
            throw new InvalidOperationException("Unable to find muling request with id " + mulingRequestId);
        }

        request.Completed = true;
        
        if (request.Type == MulingRequestType.Withdraw)
        {
            Gold -= request.Amount;
        }
        else
        {
            Gold += request.Amount;
        }
    }
    
    public MulingRequest CreateMulingRequest(long accountId, MulingRequestType type, int amount)
    {
        if (_mulingRequests.Any(r => r.AccountId == accountId && !r.Completed))
        {
            throw new InvalidOperationException("A non completed mule request already exists for this account");
        }

        var request = new MulingRequest(accountId, type, amount);
        _mulingRequests.Add(request);
        return request;
    }

    private readonly List<MulingRequest> _mulingRequests = new();

    private ActiveMule()
    {
        Position = null!;
    }
    
    public ActiveMule(long clientId, Position position, int world, DateTimeOffset utcNow)
    {
        ClientId = clientId;
        Position = position;
        World = world;
        LastHeartbeat = utcNow;
    }
}
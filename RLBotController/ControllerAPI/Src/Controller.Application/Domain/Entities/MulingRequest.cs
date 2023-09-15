using System;
using Controller.Application.Domain.Enums;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public class MulingRequest : Entity<long>
{
    public long AccountId { get; private set; }

    public int Amount { get; private set; }

    public bool Completed
    {
        get => _completed;
        set
        {
            if (_completed)
            {
                throw new InvalidOperationException("Cannot update complete on a request that is already completed");
            }

            _completed = value;
        }
    }

    private bool _completed;
    
    public MulingRequestType Type { get; private set; }
    
    public MulingRequest(long accountId, MulingRequestType type, int amount)
    {
        AccountId = accountId;
        Type = type;
        Amount = amount;
    }
}
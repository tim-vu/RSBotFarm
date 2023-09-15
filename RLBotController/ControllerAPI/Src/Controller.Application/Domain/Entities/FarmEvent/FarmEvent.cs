using System;
using Controller.Application.Domain.Events;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities.FarmEvent;

public abstract class FarmEvent : Entity<long>
{
    public DateTimeOffset At { get; private set; }

    protected FarmEvent()
    {
        
    }
    
    protected FarmEvent(DateTimeOffset at)
    {
        At = at;
        AddEvent(new FarmEventOccurredEvent(this));
    }

    public abstract string GetMessage();
}
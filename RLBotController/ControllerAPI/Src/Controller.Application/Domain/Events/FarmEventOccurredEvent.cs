using Controller.Application.Domain.Entities.FarmEvent;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class FarmEventOccurredEvent : BaseDomainEvent
{
    public FarmEvent FarmEvent { get; private set; }

    public FarmEventOccurredEvent(FarmEvent farmEvent)
    {
        FarmEvent = farmEvent;
    }
}
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Enums;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class ControlStateStatusChangedEvent : BaseDomainEvent
{
    public ControlState ControlState { get; }
    
    public ControlStateChangeInitiator Initiator { get; }
    
    public string? Reason { get; }

    public ControlStateStatusChangedEvent(
        ControlState controlState,
        ControlStateChangeInitiator initiator,
        string? reason)
    {
        ControlState = controlState;
        Initiator = initiator;
        Reason = reason;
    }
}
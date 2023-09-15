using System;
using Controller.Application.Domain.Enums;

namespace Controller.Application.Domain.Entities.FarmEvent;

public class ControlStateStatusChangedEvent : FarmEvent
{
    public ControlStateChangeInitiator Initiator { get; private set; }
    
    public FarmStatus Status { get; private set; }
    
    public string? Reason { get; private set; }

    public ControlStateStatusChangedEvent(ControlStateChangeInitiator initiator, FarmStatus status,
        DateTimeOffset at) : base(at)
    {
        Initiator = initiator;
        Status = status;
    }
    
    public override string GetMessage()
    {
        var baseMessage = Status switch
        {
            FarmStatus.Running => "The farm was started",
            FarmStatus.Paused => "The farm was paused",
            FarmStatus.Stopped => "The farm was stopped",
            _ => throw new ArgumentOutOfRangeException()
        };

        if (Initiator == ControlStateChangeInitiator.User)
        {
            return baseMessage;
        }

        return $"{baseMessage} because {Reason}";
    }
}
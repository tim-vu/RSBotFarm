using System;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.Events;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public class ControlState : Entity<long>
{
    public FarmStatus Status { get; private set; }

    public void Pause(ControlStateChangeInitiator initiator)
    {
        if (Status != FarmStatus.Running)
        {
            throw new InvalidOperationException($"Cannot pause if {nameof(Status)} is not {Enum.GetName(FarmStatus.Running)}");
        }

        AddEvent(new ControlStateStatusChangedEvent(this, initiator, null));
        Status = FarmStatus.Paused;
    }

    public void Run(ControlStateChangeInitiator initiator)
    {
        if (Status != FarmStatus.Paused && Status != FarmStatus.Stopped)
        {
            throw new InvalidOperationException(
                $"Cannot run if {nameof(Status)} is not {Enum.GetName(FarmStatus.Paused)} or {Enum.GetName(FarmStatus.Stopped)}");
        }

        AddEvent(new ControlStateStatusChangedEvent(this, initiator, null));
        Status = FarmStatus.Running; }

    public void Stop(ControlStateChangeInitiator initiator, string? reason)
    {
        if (Status != FarmStatus.Running && Status != FarmStatus.Paused)
        {
            throw new InvalidOperationException(
                $"Cannot stop if {nameof(Status)} is not {Enum.GetName(FarmStatus.Running)} or {Enum.GetName(FarmStatus.Paused)}");
        }

        AddEvent(new ControlStateStatusChangedEvent(this, initiator, reason));
        Status = FarmStatus.Stopped;
    }

    public ControlState()
    {
        Status = FarmStatus.Stopped;
    }
}
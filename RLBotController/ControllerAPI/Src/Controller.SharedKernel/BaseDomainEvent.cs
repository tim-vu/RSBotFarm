using System;
using MediatR;

namespace Controller.SharedKernel;

public abstract class BaseDomainEvent : INotification
{
    public DateTimeOffset OccuredAt { get; } = DateTimeOffset.UtcNow;
}
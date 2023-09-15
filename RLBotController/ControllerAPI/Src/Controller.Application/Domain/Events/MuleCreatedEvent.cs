using Controller.Application.Domain.Entities;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class MuleCreatedEvent : BaseDomainEvent
{
    public MuleAccount MuleAccount { get; }

    public MuleCreatedEvent(MuleAccount muleAccount)
    {
        MuleAccount = muleAccount;
    }
}
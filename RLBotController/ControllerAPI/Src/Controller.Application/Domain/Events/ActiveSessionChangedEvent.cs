using Controller.Application.Domain.Entities;
using Controller.Application.Domain.ValueObjects;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class ActiveSessionChangedEvent : BaseDomainEvent
{
    public Client Client { get; }

    public ActiveSession? PreviousSession { get; }
    
    public ActiveSessionChangedEvent(Client client, ActiveSession? previousSession)
    {
        Client = client;
        PreviousSession = previousSession;
    }
}
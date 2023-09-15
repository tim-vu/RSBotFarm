using Controller.Application.Domain.Entities;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class ClientStatusChangedEvent : BaseDomainEvent
{
    public Client Client { get; }
    
    public ClientStatusChangedEvent(Client client)
    {
        Client = client;
    }
}
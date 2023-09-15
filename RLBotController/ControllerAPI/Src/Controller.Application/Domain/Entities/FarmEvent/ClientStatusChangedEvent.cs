using System;
using Controller.Application.Domain.Enums;

namespace Controller.Application.Domain.Entities.FarmEvent;

public class ClientStatusChangedEvent : FarmEvent
{
    public long ClientId { get; private set; }
    
    
    public ClientStatus Status { get; private set; }

    // ReSharper disable once UnusedMember.Local
    private ClientStatusChangedEvent()
    {
        
    }
    
    public ClientStatusChangedEvent(Client client, DateTimeOffset at) : base(at)
    {
        ClientId = client.Id;
        Status = client.Status;
    }

    public override string GetMessage()
    {
        return Status switch
        {
            ClientStatus.Starting => $"Client {ClientId} is starting",
            ClientStatus.Running => $"Client {ClientId} is running",
            ClientStatus.StopRequested => $"Client {ClientId} is stopping",
            ClientStatus.Stopped => $"Client {ClientId} has stopped",
            _ => throw new ArgumentOutOfRangeException()
        };
    }
}
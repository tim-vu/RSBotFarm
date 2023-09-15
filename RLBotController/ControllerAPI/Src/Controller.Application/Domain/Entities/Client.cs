using System;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.Events;
using Controller.Application.Domain.ValueObjects;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public class Client : Entity<long>
{
    public string BotId { get; private set; }

    public DateTimeOffset? StartedAt { get; private set; }

    public DateTimeOffset? StoppedAt { get; private set; }
    
    public DateTimeOffset? LastClientHeartbeat { get; private set; }

    public ClientStatus Status { get; private set; } = ClientStatus.Created;

    private int? _vncPort;

    public int? VncPort
    {
        get => _vncPort;
        set
        {
            if (_vncPort != null)
            {
                throw new InvalidOperationException("The VNC port cannot be set twice");
            }

            _vncPort = value;
        }
    }
    
    private int? _websocketPort;

    public int? WebsocketPort
    {
        get => _websocketPort;
        set
        {
            if (_websocketPort != null)
            {
                throw new InvalidOperationException("The websocket port cannot be set twice");
            }

            _websocketPort = value;
        }
    }
    
    public ActiveSession ActiveSession { get; private set; }

    private Client()
    {
        BotId = null!;
        ActiveSession = null!;
    }
    
    public Client(string botId, ActiveSession activeSession)
    {
        BotId = botId;
        ActiveSession = activeSession;
    }

    public void ClientHeartbeat(DateTimeOffset utcNow)
    {
        if (utcNow < LastClientHeartbeat)
        {
            throw new ArgumentException("Heartbeat cannot be before the previous Heartbeat");
        }

        LastClientHeartbeat = utcNow;

        if (Status == ClientStatus.Starting)
        {
            AddEvent(new ClientStatusChangedEvent(this));
            Status = ClientStatus.Running;
        }
    }

    public void Start(DateTimeOffset utcNow)
    {
        if (Status != ClientStatus.Created)
        {
            throw new InvalidOperationException("You can only start a client which was just created");
        }

        AddEvent(new ClientStatusChangedEvent(this));
        Status = ClientStatus.Starting;
        StartedAt = utcNow;
    }

    public void Stop(DateTimeOffset utcNow)
    {
        AddEvent(new ClientStatusChangedEvent(this));
        Status = ClientStatus.Stopped;
        StoppedAt = utcNow;
    }

    public void RequestGracefulStop()
    {
        if (Status is ClientStatus.StopRequested or ClientStatus.Stopped)
        {
            throw new InvalidOperationException("Graceful stop can only be request if the client is running");
        }
        
        AddEvent(new ClientStatusChangedEvent(this));
        Status = ClientStatus.StopRequested;
    }
}
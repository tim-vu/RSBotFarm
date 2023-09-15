using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Interfaces;
using Controller.Application.Domain.Enums;
using Controller.Application.Features.ControlState.Models;
using Controller.SharedKernel;
using ControlStateStatusChangedEvent = Controller.Application.Domain.Events.ControlStateStatusChangedEvent;

namespace Controller.Application.Features.ControlState.Handlers;

public class ControlStateStatusChangedEventHandler : IDomainEventHandler<Domain.Events.ControlStateStatusChangedEvent>
{
    private readonly IServerSentEventService _serverSentEventService;

    public ControlStateStatusChangedEventHandler(IServerSentEventService serverSentEventService)
    {
        _serverSentEventService = serverSentEventService;
    }

    public Task Handle(ControlStateStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        if (notification.Initiator == ControlStateChangeInitiator.User)
        {
            return Task.CompletedTask;
        }
        
        var controlStateVm = new ControlStateVm(notification.ControlState.Status);

        return _serverSentEventService.Send(controlStateVm);
    }
}
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Interfaces;
using Controller.Application.Domain.Events;
using Controller.Application.Features.Events.Models;
using Controller.SharedKernel;

namespace Controller.Application.Features.Events.Handlers;

public class FarmEventOccurredEventHandler : IDomainEventHandler<FarmEventOccurredEvent>
{
    private readonly IServerSentEventService _serverSentEventService;

    public FarmEventOccurredEventHandler(IServerSentEventService serverSentEventService)
    {
        _serverSentEventService = serverSentEventService;
    }

    public Task Handle(FarmEventOccurredEvent notification, CancellationToken cancellationToken)
    {
        var eventVm = new EventVm(
            notification.FarmEvent.Id,
            notification.FarmEvent.At.ToUnixTimeSeconds(),
            notification.FarmEvent.GetMessage()
        );

        return _serverSentEventService.Send(eventVm);
    }
}
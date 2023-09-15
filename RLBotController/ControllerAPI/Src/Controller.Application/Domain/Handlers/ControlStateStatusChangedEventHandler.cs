using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Events;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Handlers;

public class ControlStateStatusChangedEventHandler : IDomainEventHandler<ControlStateStatusChangedEvent>
{
    private readonly RSBControllerContext _context;

    public ControlStateStatusChangedEventHandler(RSBControllerContext context)
    {
        _context = context;
    }

    public async Task Handle(ControlStateStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        var farmEvent = new Entities.FarmEvent.ControlStateStatusChangedEvent(
            notification.Initiator,
            notification.ControlState.Status,
            notification.OccuredAt
        );

        _context.FarmEvents.Add(farmEvent);
        await _context.SaveChangesAsync(cancellationToken);
    }
}
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Entities;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Handlers;

public class WorkerCreatedEventHandler : IDomainEventHandler<Events.WorkerCreatedEvent>
{
    private readonly RSBControllerContext _context;

    public WorkerCreatedEventHandler(RSBControllerContext context)
    {
        _context = context;
    }

    public async Task Handle(Events.WorkerCreatedEvent notification, CancellationToken cancellationToken)
    {
        var schedule = new Schedule();
        _context.Schedules.Add(schedule);
        await _context.SaveChangesAsync(cancellationToken);
    }
}
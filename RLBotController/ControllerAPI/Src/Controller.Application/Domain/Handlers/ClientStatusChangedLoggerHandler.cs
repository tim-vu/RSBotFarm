using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.Events;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Handlers;

public class ClientStatusChangedLoggerHandler : IDomainEventHandler<ClientStatusChangedEvent>
{
    private readonly RSBControllerContext _context;

    public ClientStatusChangedLoggerHandler(RSBControllerContext context)
    {
        _context = context;
    }

    public async Task Handle(ClientStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        if (notification.Client.Status == ClientStatus.Created)
        {
            return;
        }
        
        _context.Add(new Entities.FarmEvent.ClientStatusChangedEvent(
            notification.Client,
            notification.OccuredAt
            )
        );
        await _context.SaveChangesAsync(cancellationToken);
    }
}
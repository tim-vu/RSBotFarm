using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Events;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Domain.Handlers;

public class ClientStatusChangedMuleHandler : IDomainEventHandler<ClientStatusChangedEvent>
{
    private readonly RSBControllerContext _context;

    public ClientStatusChangedMuleHandler(RSBControllerContext context)
    {
        _context = context;
    }

    public async Task Handle(ClientStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        var mule = await _context.Mules.FirstOrDefaultAsync(m => m.ClientId == notification.Client.Id, cancellationToken);
        
        mule?.Deactivate();

        await _context.SaveChangesAsync(cancellationToken);
    }
}
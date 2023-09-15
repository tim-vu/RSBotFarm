using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Events;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Handlers;

public class AccountStatusChangedEventHandler : IDomainEventHandler<AccountStatusChangedEvent>
{
    private readonly RSBControllerContext _context;

    public AccountStatusChangedEventHandler(RSBControllerContext context)
    {
        _context = context;
    }

    public async Task Handle(AccountStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        var accountEvent =
            new Entities.FarmEvent.AccountStatusChangedEvent(notification.Account, notification.OccuredAt);

        _context.FarmEvents.Add(accountEvent);

        await _context.SaveChangesAsync(cancellationToken);
    }
}
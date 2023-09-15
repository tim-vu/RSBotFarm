using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Entities.FarmEvent;
using Controller.Application.Domain.Events;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Domain.Handlers;

public class MulingRequestCompletedHandler : IDomainEventHandler<MulingRequestCompleted>
{
    private readonly RSBControllerContext _context;

    public MulingRequestCompletedHandler(RSBControllerContext context)
    {
        _context = context;
    }

    public async Task Handle(MulingRequestCompleted notification, CancellationToken cancellationToken)
    {
        var account = await _context.Accounts.SingleOrDefaultAsync(a => a.Id == notification.MulingRequest.AccountId, cancellationToken);

        _context.FarmEvents.Add(new MulingCompleted(
            notification.MulingRequest.Id,
            account!.Username,
            notification.MulingRequest.Amount,
            notification.OccuredAt)
        );

        await _context.SaveChangesAsync(cancellationToken);
    }
}
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.Events;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;
using Microsoft.Extensions.Logging;

namespace Controller.Application.Domain.Handlers;

public class ClientStatusChangedAccountHandler : IDomainEventHandler<ClientStatusChangedEvent>
{
    private readonly RSBControllerContext _context;
    private readonly ILogger<ClientStatusChangedAccountHandler> _logger;

    public ClientStatusChangedAccountHandler(RSBControllerContext context, ILogger<ClientStatusChangedAccountHandler> logger)
    {
        _context = context;
        _logger = logger;
    }

    public async Task Handle(ClientStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        if (notification.Client.Status != ClientStatus.Created)
        {
            return;
        }

        var account = await _context.Accounts.FindAsync(notification.Client.ActiveSession.AccountId);

        if (account == null)
        {
            return;
        }

        if (account.ScriptAssigment == null)
        {
            account.Assign(notification.Client.ActiveSession.ScriptId, notification.OccuredAt);
            await _context.SaveChangesAsync(cancellationToken);
            return;
        }

        if (account.ScriptAssigment.ScriptId != notification.Client.ActiveSession.ScriptId)
        {
            _logger.LogWarning("Client {ClientId} was started with script {ScriptId} and account {AccountId} while the account was already assigned to {ScriptId}", 
                notification.Client.Id, 
                notification.Client.ActiveSession.ScriptId, 
                account.Id,
                account.ScriptAssigment.ScriptId
            );
        }
    }
}
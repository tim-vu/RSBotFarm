using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Interfaces;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Events;
using Controller.Application.Features.Accounts.Mules;
using Controller.Application.Features.Accounts.Workers.Models;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;

namespace Controller.Application.Features.Accounts.Handlers;

public class AccountStatusChangedEventHandler : IDomainEventHandler<AccountStatusChangedEvent>
{
    private readonly RSBControllerContext _context;
    private readonly IServerSentEventService _serverSentEventService;

    public AccountStatusChangedEventHandler(RSBControllerContext context, IServerSentEventService serverSentEventService)
    {
        _context = context;
        _serverSentEventService = serverSentEventService;
    }

    public Task Handle(AccountStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        if (notification.Account is WorkerAccount worker)
        {
            var workerVm = new WorkerAccountVm(
                worker.Id,
                worker.Username,
                worker.Password,
                worker.AccountStatus,
                worker.MembershipExpiry?.ToUnixTimeSeconds()
            );

            return _serverSentEventService.Send(workerVm);
        }

        if (notification.Account is MuleAccount mule)
        {
            var muleVm = new GetMules.MuleAccountVm(
                mule.Id,
                mule.Username,
                mule.Password,
                mule.AccountStatus,
                mule.MembershipExpiry?.ToUnixTimeSeconds()
            );

            return _serverSentEventService.Send(muleVm);
        }

        return Task.CompletedTask;
    }
}
using System;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Accounts.Workers.Models;
using Controller.Application.Infrastructure.Persistence;
using MediatR;

namespace Controller.Application.Features.Accounts.Workers;

public class AddWorkerAccount
{
    public sealed record Command(string Username, string Password, DateTimeOffset? MembershipExpiry) : IQuery<WorkerAccountVm>;

    public class Handler : IRequestHandler<Command, WorkerAccountVm>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task<WorkerAccountVm> Handle(Command request, CancellationToken cancellationToken)
        {
            var account = new WorkerAccount(request.Username, request.Password, request.MembershipExpiry);

            _context.Add(account);

            await _context.SaveChangesAsync(cancellationToken);

            return new WorkerAccountVm(
                account.Id,
                account.Username,
                account.Password,
                account.AccountStatus,
                account.MembershipExpiry?.ToUnixTimeSeconds()
            );
        }
    }
}
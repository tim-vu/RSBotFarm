using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Domain.Entities;
using Controller.Application.Infrastructure.Persistence;
using MediatR;

namespace Controller.Application.Features.Accounts.Workers;

public class BulkAddWorkerAccount
{
    public sealed record Command(Worker[] Accounts) : ICommand;

    public sealed record Worker(string Username, string Password, DateTimeOffset? MembershipExpiry);

    public sealed class Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var accounts = request.Accounts
                .Select(account => new WorkerAccount(
                    account.Username, 
                    account.Password, 
                    account.MembershipExpiry)
                ).ToList();

            _context.Accounts.AddRange(accounts);

            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
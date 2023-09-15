using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Features.Accounts.Workers.Models;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Accounts.Workers;

public class GetWorkerAccounts
{
    public sealed record Query : IQuery<WorkerAccountVm[]>;
    
    public sealed class Handler : IRequestHandler<Query, WorkerAccountVm[]>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task<WorkerAccountVm[]> Handle(Query request, CancellationToken cancellationToken)
        {
            var accounts = await _context.WorkerAccounts.ToListAsync(cancellationToken);
            return accounts.Select(w => new WorkerAccountVm(
                w.Id,
                w.Username,
                w.Password,
                w.AccountStatus,
                w.MembershipExpiry?.ToUnixTimeSeconds())
            ).ToArray();
        }
    }
}
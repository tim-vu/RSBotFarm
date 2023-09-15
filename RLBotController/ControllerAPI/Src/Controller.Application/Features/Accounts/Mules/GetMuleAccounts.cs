using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Domain.Enums;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Accounts.Mules;

public class GetMules
{
    public sealed record Query : IQuery<MuleAccountVm[]>;

    public sealed record MuleAccountVm(long Id, string Username, string Password, AccountStatus Status, long? MembershipExpiry);
    
    public class Handler : IRequestHandler<Query, MuleAccountVm[]>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task<MuleAccountVm[]> Handle(Query request, CancellationToken cancellationToken)
        {
            var mules = await _context.MuleAccounts
                .ToListAsync(cancellationToken);

            return mules.Select(m =>
                    new MuleAccountVm(
                        m.Id,
                        m.Username,
                        m.Password,
                        m.AccountStatus,
                        m.MembershipExpiry?.ToUnixTimeMilliseconds()))
                .ToArray();
        }
    }
}
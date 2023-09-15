using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Exceptions;
using Controller.Application.Common.Services.ClientContext;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Muling;

public class GetActiveMulingRequests
{
    public sealed record Query : IQuery<ActiveMulingRequestVm[]>;

    public sealed record ActiveMulingRequestVm(long Id, string DisplayName);

    public class Handler : IRequestHandler<Query, ActiveMulingRequestVm[]>
    {
        private readonly RSBControllerContext _context;
        private readonly IClientContext _clientContext;

        public Handler(RSBControllerContext context, IClientContext clientContext)
        {
            _context = context;
            _clientContext = clientContext;
        }

        public async Task<ActiveMulingRequestVm[]> Handle(Query request, CancellationToken cancellationToken)
        {
            var mule = await _context.Clients
                .Where(c => c.Id == _clientContext.ClientId)
                .Join(_context.Mules.Include(m => m.MulingRequests),
                    c => c.Id,
                    m => m.ClientId,
                    (c, m) => m
                )
                .FirstOrDefaultAsync(cancellationToken);

            if (mule == null)
            {
                throw new NotFoundException("Unable to find mule associated with client with ClientId " + _clientContext.ClientId);
            }

            var accountIds = mule.MulingRequests.Select(r => r.AccountId).ToHashSet();

            var playerDetails = await _context.PlayerDetails
                .Where(a => accountIds.Contains(a.AccountId))
                .ToDictionaryAsync(a => a.AccountId, cancellationToken);
            
            return mule.MulingRequests
                .Select(r => new ActiveMulingRequestVm(r.Id, playerDetails[r.AccountId].DisplayName))
                .ToArray();
        }
    }
}
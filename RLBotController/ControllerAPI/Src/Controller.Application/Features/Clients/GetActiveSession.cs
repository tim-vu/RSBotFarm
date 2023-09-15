using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Services.ClientContext;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Clients;

public class GetActiveSession
{
    public sealed record Query : IQuery<ActiveSessionVm>;

    public sealed record ActiveSessionVm(string Script, ActiveSessionAccountVm Account);

    public sealed record ActiveSessionAccountVm(long Id, string Username, string Password);

    public class Handler : IRequestHandler<Query, ActiveSessionVm>
    {
        private readonly RSBControllerContext _context;
        private readonly IClientContext _clientContext;

        public Handler(RSBControllerContext context, IClientContext clientContext)
        {
            _context = context;
            _clientContext = clientContext;
        }

        public async Task<ActiveSessionVm> Handle(Query request, CancellationToken cancellationToken)
        {
            var result = await _context.Clients
                .Join(
                    _context.Accounts,
                    c => c.ActiveSession.AccountId,
                    a => a.Id,
                    (c, a) => new { Client = c, Account = a }
                )
                .Join(
                    _context.Scripts,
                    ca => ca.Client.ActiveSession.ScriptId,
                    s => s.Id,
                    (ca, s) => new { Client = ca.Client, Account = ca.Account, Script = s }
                )
                .SingleOrDefaultAsync(p => p.Client.Id == _clientContext.ClientId, cancellationToken);
            
            return new ActiveSessionVm(
                result!.Script.Name,
                new ActiveSessionAccountVm(
                    result.Account.Id,
                    result.Account.Username,
                    result.Account.Password
                )
            );
        }
    }
}
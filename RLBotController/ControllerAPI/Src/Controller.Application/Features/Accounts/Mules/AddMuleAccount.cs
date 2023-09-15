using System;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Services.Clock;
using Controller.Application.Domain.Constants;
using Controller.Application.Domain.Entities;
using Controller.Application.Infrastructure.Persistence;
using MediatR;

namespace Controller.Application.Features.Accounts.Mules;

public class AddMuleAccount
{
    public sealed record Command(string Username, string Password, int Gold, DateTimeOffset? MembershipExpiry) : ICommand;
    
    public sealed record Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;
        private readonly IClock _clock;

        public Handler(RSBControllerContext context, IClock clock)
        {
            _context = context;
            _clock = clock;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var mule = new MuleAccount(request.Username, request.Password, request.MembershipExpiry);

            mule.Assign(ScriptConstants.MuleScriptId, _clock.UtcNow);
            
            _context.MuleAccounts.Add(mule);

            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Exceptions;
using Controller.Application.Common.Services.ClientContext;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.ValueObjects;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Muling;

public class CreateMulingRequest
{
    public sealed record Command(MulingRequestType Type, int Amount) : ICommand<MulingRequestVm?>;
    
    public sealed record MulingRequestVm(long Id, string DisplayName, Position Position, int World);

    public class Handler : IRequestHandler<Command, MulingRequestVm?>
    {
        private readonly RSBControllerContext _context;
        private readonly IClientContext _clientContext;
        
        public Handler(RSBControllerContext context, IClientContext clientContext)
        {
            _context = context;
            _clientContext = clientContext;
        }

        public async Task<MulingRequestVm?> Handle(Command request, CancellationToken cancellationToken)
        {
            var client = await _context.Clients.FindAsync(new object?[] { _clientContext.ClientId }, cancellationToken);

            var muleQuery = _context.Clients
                .Join(
                    _context.Mules,
                    c => c.Id,
                    m => m.ClientId,
                    (c, m) => new { Client = c, Mule = m }
                )
                .Join(
                    _context.PlayerDetails,
                    p => p.Client.ActiveSession.AccountId,
                    d => d.AccountId,
                    (p, d) => new { p.Client, p.Mule, PlayerDetails = d }
                ).Select(p => new
                {
                    Mule = p.Mule,
                    PlayerDetails = p.PlayerDetails
                });

            if (request.Type == MulingRequestType.Withdraw)
            {
                muleQuery = muleQuery.Where(m => m.Mule.Gold > request.Amount);
            }

            var selectedMule = await muleQuery.FirstOrDefaultAsync(cancellationToken);

            if (selectedMule == null)
            {
                return null;
                throw new InvalidStateException("No mule is currently available to fulfill this request");
            }

            var mulingRequest = selectedMule.Mule.CreateMulingRequest(client!.ActiveSession.AccountId, request.Type, request.Amount);

            await _context.SaveChangesAsync(cancellationToken);

            return new MulingRequestVm(
                mulingRequest.Id, 
                selectedMule.PlayerDetails.DisplayName,
                selectedMule.Mule.Position,
                selectedMule.Mule.World
            );
        }
    }
}
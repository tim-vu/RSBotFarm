using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Services.ClientContext;
using Controller.Application.Common.Services.Clock;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.ValueObjects;
using Controller.Application.Infrastructure.Persistence;
using MediatR;

namespace Controller.Application.Features.Muling;

public class CreateMule
{
    public sealed record Command(Position Position, int World) : ICommand;
    
    public class Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;
        private readonly IClientContext _clientContext;
        private readonly IClock _clock;

        public Handler(RSBControllerContext context, IClientContext clientContext, IClock clock)
        {
            _context = context;
            _clock = clock;
            _clientContext = clientContext;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var client = await _context.Clients.FindAsync(new object?[] { _clientContext.ClientId }, cancellationToken);

            var mule = new ActiveMule(client!.Id, request.Position, request.World, _clock.UtcNow);

            _context.Mules.Add(mule);

            await _context.SaveChangesAsync(cancellationToken); 
        }
    }
}
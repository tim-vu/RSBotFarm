using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Services.ClientContext;
using Controller.Application.Common.Services.Clock;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.Extensions.Logging;

namespace Controller.Application.Features.Clients;

public class SignalStop
{
    public sealed record Command() : ICommand;
    
    public class Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;
        private readonly IClientContext _clientContext;
        private readonly IClock _clock;
        private readonly ILogger<Handler> _logger;

        public Handler(RSBControllerContext context, IClientContext clientContext, ILogger<Handler> logger, IClock clock)
        {
            _context = context;
            _logger = logger;
            _clock = clock;
            _clientContext = clientContext;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var client = await _context.Clients.FindAsync(new object?[] { _clientContext.ClientId }, cancellationToken: cancellationToken);

            if (client == null)
            {
                return;
            }

            _logger.LogInformation("Client with BotId {BotId} stopped", client.BotId);
            client.Stop(_clock.UtcNow);
            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
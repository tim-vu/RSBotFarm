using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Domain.Enums;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace Controller.Application.Features.ControlState;

public class UpdateControlState
{
    public sealed record Command(FarmStatus Status) : ICommand;
    
    public class Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;
        private readonly ILogger<Handler> _logger; 

        public Handler(RSBControllerContext context, ILogger<Handler> logger)
        {
            _context = context;
            _logger = logger;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var controlState = await _context.ControlStates.SingleAsync(cancellationToken);

            var initialStatus = controlState.Status;
            
            if (request.Status == FarmStatus.Running)
            {
                controlState.Run(ControlStateChangeInitiator.User);
            }

            if (request.Status == FarmStatus.Paused)
            {
                controlState.Pause(ControlStateChangeInitiator.User);
            }

            if (request.Status == FarmStatus.Stopped)
            {
                controlState.Stop(ControlStateChangeInitiator.User, null);
            }

            _logger.LogInformation("{FarmStatusName} changed from {FarmStatus} to {FarmStatus}", nameof(FarmStatus), initialStatus, controlState.Status);
            
            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
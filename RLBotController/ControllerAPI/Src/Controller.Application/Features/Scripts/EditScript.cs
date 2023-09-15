using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Exceptions;
using Controller.Application.Domain.Entities;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Scripts;

public class EditScript
{
    public sealed class Command : ICommand
    {
        public long Id { get; set; }
        
        public int Instances { get; set; }
        
        public int? Priority { get; set; }
    }
    
    public class Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var script = await _context.Scripts.FirstOrDefaultAsync(s => s.Id == request.Id, cancellationToken);

            if (script == null)
            {
                throw new NotFoundException($"script with id {request.Id} not found"); 
            }

            script.Instances = request.Instances;

            if (request.Priority != null)
            {
                if (script is not WorkerScript workerScript)
                {
                    throw new NotAllowedException("You are not allowed to update the priority for this script");
                }

                workerScript.Priority = request.Priority.Value;
            }

            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
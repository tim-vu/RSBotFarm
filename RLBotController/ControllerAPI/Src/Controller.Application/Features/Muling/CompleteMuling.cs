using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Exceptions;
using Controller.Application.Domain.Entities;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Muling;

public class CompleteMuling
{
    public sealed record Command(long Id) : ICommand;
    
    public class Handler : IRequestHandler<Command>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var mule = await _context.Mules
                .Where(m => m.MulingRequests.Any(r => r.Id == request.Id))
                .SingleOrDefaultAsync(r => r.Id == request.Id, cancellationToken);

            if (mule == null)
            {
                throw new NotFoundException($"Unable to find {nameof(MulingRequest)} with id {request.Id}");
            }
            
            mule.CompleteMulingRequest(request.Id);
            
            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
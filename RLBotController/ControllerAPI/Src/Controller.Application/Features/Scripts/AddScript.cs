using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Domain.Entities;
using Controller.Application.Features.Scripts.Models;
using Controller.Application.Infrastructure.Persistence;
using MediatR;

namespace Controller.Application.Features.Scripts;

public class AddScript
{
    public sealed record Command(string Name, int Instances, int Priority) : ICommand;

    public class Handler : IRequestHandler<Command >
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task Handle(Command request, CancellationToken cancellationToken)
        {
            var script = new WorkerScript(request.Name, request.Instances, request.Priority);
            _context.Scripts.Add(script);
            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
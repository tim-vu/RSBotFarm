using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Features.Scripts.Models;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Scripts;

public class GetScripts
{
    public sealed record Query : IQuery<ScriptVm[]>;
    
    public class Handler : IRequestHandler<Query, ScriptVm[]>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task<ScriptVm[]> Handle(Query request, CancellationToken cancellationToken)
        {
            var scripts = await _context.Scripts
                .ToListAsync(cancellationToken);

            return scripts.Select(p => new ScriptVm(
                p.Id, 
                p.Name, 
                p.Instances, 
                p.Priority
            )).ToArray();
        }
    }
}
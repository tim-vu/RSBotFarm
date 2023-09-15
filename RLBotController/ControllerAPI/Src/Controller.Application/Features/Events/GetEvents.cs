using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Features.Events.Models;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Events;

public partial class GetEvents
{
    public sealed record Query : IQuery<EventVm[]>;

    public class Handler : IRequestHandler<Query, EventVm[]>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task<EventVm[]> Handle(Query request, CancellationToken cancellationToken)
        {
            var events = await _context.FarmEvents
                .ToListAsync(cancellationToken);

            return events.Select(e => new EventVm(e.Id, e.At.ToUnixTimeSeconds(), e.GetMessage())).ToArray();
        }
    }
}
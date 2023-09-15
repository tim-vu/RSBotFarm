using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Features.ControlState.Models;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.ControlState;

public class GetControlState
{
    public sealed record Query : IQuery<ControlStateVm>;

    public class Handler : IRequestHandler<Query, ControlStateVm>
    {
        private readonly RSBControllerContext _context;

        public Handler(RSBControllerContext context)
        {
            _context = context;
        }

        public async Task<ControlStateVm> Handle(Query request, CancellationToken cancellationToken)
        {
            var controlState = await _context.ControlStates.SingleAsync(cancellationToken);

            return new ControlStateVm(controlState.Status);
        }
    }
}
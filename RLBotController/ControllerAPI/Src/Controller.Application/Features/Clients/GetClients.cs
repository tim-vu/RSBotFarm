using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Services.Clock;
using Controller.Application.Domain.Enums;
using Controller.Application.Features.Clients.Models;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Clients;

public class GetClients
{
    public sealed record Query : IQuery<ClientVm[]>;
    
    public class Handler : IRequestHandler<Query, ClientVm[]>
    {
        private readonly RSBControllerContext _context;
        private readonly IClock _clock;

        public Handler(RSBControllerContext context, IClock clock)
        {
            _context = context;
            _clock = clock;
        }

        public async Task<ClientVm[]> Handle(Query request, CancellationToken cancellationToken)
        {
            var clients = await _context.Clients
                .Where(c => c.Status != ClientStatus.Stopped)
                .ToListAsync(cancellationToken);

            var scriptIds = clients
                .Select(c => c.ActiveSession?.ScriptId)
                .Where(i => i != null)
                .Cast<long>()
                .ToHashSet();

            var scripts = await _context.Scripts
                .Where(s => scriptIds.Contains(s.Id))
                .ToDictionaryAsync(s => s.Id, cancellationToken);

            return clients.Select(c => new ClientVm(
                c.Id, 
                c.BotId,
                c.Status,
                c.StartedAt == DateTimeOffset.MinValue ? int.MaxValue : c.StartedAt?.ToUnixTimeSeconds(),
                c.ActiveSession == null ? null : new ActiveScriptVm(
                    c.ActiveSession.ScriptId,
                    scripts[c.ActiveSession.ScriptId].Name),
                c.WebsocketPort)
            ).ToArray();
        }
    }
}
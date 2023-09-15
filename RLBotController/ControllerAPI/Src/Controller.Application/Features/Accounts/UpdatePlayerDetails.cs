using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Enums;
using Controller.Application.Infrastructure.Persistence;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Features.Accounts;

public class UpdatePlayerDetails
{
    public sealed class Command : ICommand
    {
        public long Id { get; set; }
        
        public string DisplayName { get; set; } = null!;

        public Dictionary<Skill, int> Skills { get; set; } = null!;
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
            var playerDetails = await _context.PlayerDetails
                .Where(d => d.AccountId == request.Id)
                .FirstOrDefaultAsync(cancellationToken);

            if (playerDetails == null)
            {
                playerDetails = new PlayerDetails(request.Id, request.DisplayName, request.Skills);
                _context.PlayerDetails.Add(playerDetails);
            }
            else
            {
                playerDetails.DisplayName = request.DisplayName;
                playerDetails.Skills = request.Skills;
            }

            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
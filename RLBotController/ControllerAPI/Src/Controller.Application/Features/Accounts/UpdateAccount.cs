using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Abstractions.Messaging;
using Controller.Application.Common.Exceptions;
using Controller.Application.Domain.Enums;
using Controller.Application.Infrastructure.Persistence;
using MediatR;

namespace Controller.Application.Features.Accounts;

public class UpdateAccount
{
    public sealed class Command : ICommand
    {
        public long Id { get; set; } 
        
        public AccountStatus Status { get; set; }
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
            var account = await _context.Accounts.FindAsync(request.Id);

            if (account == null)
            {
                throw new NotFoundException($"account with id {request.Id} not found");
            }

            account.AccountStatus = request.Status;

            await _context.SaveChangesAsync(cancellationToken);
        }
    }
}
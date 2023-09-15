using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Entities.FarmEvent;
using Controller.SharedKernel;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace Controller.Application.Infrastructure.Persistence;

public class RSBControllerContext : DbContext
{
    private readonly IMediator _mediator;
    
    public RSBControllerContext(DbContextOptions<RSBControllerContext> options, IMediator mediator) : base(options)
    {
        _mediator = mediator;
    }

    public DbSet<Account> Accounts { get; set; } = null!;
    
    public DbSet<WorkerAccount> WorkerAccounts { get; set; } = null!;
    
    public DbSet<MuleAccount> MuleAccounts { get; set; } = null!;
    
    public DbSet<Script> Scripts { get; set; } = null!;
    
    public DbSet<Client> Clients { get; set; } = null!;
    
    public DbSet<ControlState> ControlStates { get; set; } = null!;
    
    public DbSet<FarmEvent> FarmEvents { get; set; } = null!;
    
    public DbSet<ActiveMule> Mules { get; set; } = null!;
    
    public DbSet<MulingRequest> MulingRequests { get; set; } = null!;

    public DbSet<PlayerDetails> PlayerDetails { get; set; } = null!;

    public DbSet<Schedule> Schedules { get; set; } = null!;

    protected override void OnModelCreating(ModelBuilder builder)
    {
        builder.Ignore<BaseDomainEvent>();
        
        builder.ApplyConfigurationsFromAssembly(typeof(RSBControllerContext).Assembly);
    }
    
    public override async Task<int> SaveChangesAsync(CancellationToken cancellationToken = new())
    {
        var result = await base.SaveChangesAsync(cancellationToken).ConfigureAwait(false);

        // ignore events if no dispatcher provided
        if (_mediator == null) return result;

        var entitiesWithEvents = ChangeTracker
            .Entries()
            .Select(e => e.Entity as Entity<long>)
            .Where(e => e?.Events != null && e.Events.Any())
            .Cast<Entity<long>>()
            .ToArray();

        foreach (var entity in entitiesWithEvents)
        {
            var events = entity.Events.ToArray();
            entity.ClearEvents();
            foreach (var domainEvent in events)
            {
                await _mediator.Publish(domainEvent, cancellationToken).ConfigureAwait(false);
            }
        }

        return result;
    }
}
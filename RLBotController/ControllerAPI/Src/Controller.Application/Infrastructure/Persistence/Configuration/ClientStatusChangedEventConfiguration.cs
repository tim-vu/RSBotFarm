using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Entities.FarmEvent;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class ClientStatusChangedEventConfiguration : IEntityTypeConfiguration<ClientStatusChangedEvent>
{

    public void Configure(EntityTypeBuilder<ClientStatusChangedEvent> builder)
    {
        builder.HasOne<Client>()
            .WithMany()
            .HasForeignKey(e => e.ClientId);
        
    }
}
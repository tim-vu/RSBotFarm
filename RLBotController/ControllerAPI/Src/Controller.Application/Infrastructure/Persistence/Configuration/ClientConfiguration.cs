using Controller.Application.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class ClientConfiguration : IEntityTypeConfiguration<Client>
{
    public void Configure(EntityTypeBuilder<Client> builder)
    {
        builder.HasAlternateKey(c => c.BotId);
        builder.OwnsOne(c => c.ActiveSession);
        
        builder.HasOne<ActiveMule>()
            .WithOne()
            .HasForeignKey<ActiveMule>(m => m.ClientId)
            .IsRequired();

    }
}
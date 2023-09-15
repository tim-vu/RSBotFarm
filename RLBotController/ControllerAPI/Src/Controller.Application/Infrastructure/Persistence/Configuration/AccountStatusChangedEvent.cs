using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Entities.FarmEvent;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class AccountStatusChangedEventConfiguration : IEntityTypeConfiguration<AccountStatusChangedEvent>
{
    public void Configure(EntityTypeBuilder<AccountStatusChangedEvent> builder)
    {
        builder.HasOne<Account>()
            .WithMany()
            .HasForeignKey(e => e.AccountId);

    }
}
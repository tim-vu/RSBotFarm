using Controller.Application.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class AccountConfiguration : IEntityTypeConfiguration<Account>
{
    public void Configure(EntityTypeBuilder<Account> builder)
    {
        builder.HasAlternateKey(a => a.Username);
        builder.Property(a => a.Password).IsRequired();
        builder.OwnsOne(a => a.ScriptAssigment);

    }
}
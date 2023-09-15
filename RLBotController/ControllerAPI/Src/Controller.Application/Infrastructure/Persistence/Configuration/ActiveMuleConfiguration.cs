using Controller.Application.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class ActiveMuleConfiguration : IEntityTypeConfiguration<ActiveMule>
{
    public void Configure(EntityTypeBuilder<ActiveMule> builder)
    {
        builder.HasKey(m => m.Id);
        builder.OwnsOne(m => m.Position);
    }
}
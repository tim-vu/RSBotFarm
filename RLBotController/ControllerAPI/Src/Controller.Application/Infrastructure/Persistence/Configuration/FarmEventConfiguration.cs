using Controller.Application.Domain.Entities.FarmEvent;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class FarmEventConfiguration : IEntityTypeConfiguration<FarmEvent>
{
    public void Configure(EntityTypeBuilder<FarmEvent> builder)
    {
        builder.UseTpcMappingStrategy();
    }
}
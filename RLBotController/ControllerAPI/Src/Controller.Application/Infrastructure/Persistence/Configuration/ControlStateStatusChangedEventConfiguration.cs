using Controller.Application.Domain.Entities.FarmEvent;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class ControlStateStatusChangedEventConfiguration : IEntityTypeConfiguration<ControlStateStatusChangedEvent>
{
    public void Configure(EntityTypeBuilder<ControlStateStatusChangedEvent> builder)
    {
    }
}
using Controller.Application.Domain.Entities;
using Controller.SharedKernel;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class ControlStateConfiguration : IEntityTypeConfiguration<ControlState>
{
    public void Configure(EntityTypeBuilder<ControlState> builder)
    {
        builder.HasKey(e => e.Id);

        var initialState = new ControlState();
        var idProperty = typeof(Entity<long>).GetProperty(nameof(Entity<long>.Id));
        idProperty!.SetValue(initialState, 1);
        
        builder.HasData(initialState);
        
    }
}
using Controller.Application.Domain.Constants;
using Controller.Application.Domain.Entities;
using Controller.SharedKernel;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class ScriptConfiguration : IEntityTypeConfiguration<Script>
{
    public void Configure(EntityTypeBuilder<Script> builder)
    {
        var muleScript = new Script("Muler", 1, 0);
        var idProperty = typeof(Entity<long>).GetProperty(nameof(Entity<long>.Id));
        idProperty!.SetValue(muleScript, ScriptConstants.MuleScriptId);
        
        builder.HasData(muleScript);

        builder.HasAlternateKey(s => s.Name);
    }
}
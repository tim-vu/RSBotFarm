using Controller.Application.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace Controller.Application.Infrastructure.Persistence.Configuration;

public class PlayerDetailsConfiguration : IEntityTypeConfiguration<PlayerDetails> {
    
    private const string PlayerDetailsForeignKey = "PlayerDetailsId";
    
    public void Configure(EntityTypeBuilder<PlayerDetails> builder)
    {
        builder.HasKey(d => d.AccountId);
        
        builder.HasOne<Account>()
            .WithOne()
            .HasForeignKey<PlayerDetails>(d => d.AccountId)
            .IsRequired();

        builder.OwnsMany<SkillExperience>("_skills", b =>
        {
            b.WithOwner().HasForeignKey(PlayerDetailsForeignKey);
            b.HasKey(nameof(SkillExperience.Skill), PlayerDetailsForeignKey);
        });

        builder.Ignore(d => d.Skills);
    }
}
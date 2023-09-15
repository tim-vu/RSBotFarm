using Controller.Application.Domain.Enums;

namespace Controller.Application.Domain.Entities;

public class SkillExperience
{
    public Skill Skill { get; private set; }
    
    public int Experience { get; private set; }

    private SkillExperience()
    {
        
    }
    
    public SkillExperience(Skill skill, int experience)
    {
        Skill = skill;
        Experience = experience;
    }
}
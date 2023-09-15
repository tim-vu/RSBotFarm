using System;
using System.Collections.Generic;
using System.Linq;
using Controller.Application.Domain.Enums;

namespace Controller.Application.Domain.Entities;

public class PlayerDetails
{
    private const int MaxExperience = 200_000_000;
    
    public long AccountId { get; private set; }
    
    public string DisplayName { get; set; }

    public IReadOnlyDictionary<Skill, int> Skills
    {
        get => _skills.ToDictionary(p => p.Skill, p => p.Experience);
        set
        {
            ValidateSkillDictionary(value);
            _skills = value.Select(p => new SkillExperience(p.Key, p.Value)).ToList();
        }
    }
    
    private List<SkillExperience> _skills;

    private PlayerDetails()
    {
        DisplayName = null!;
        _skills = null!;
    }
    
    public PlayerDetails(long accountId, string displayName, IReadOnlyDictionary<Skill, int> skills)
    {
        ValidateSkillDictionary(skills);
        
        AccountId = accountId;
        DisplayName = displayName;
        _skills = skills.Select(p => new SkillExperience(p.Key, p.Value)).ToList();
    }

    private static void ValidateSkillDictionary(IReadOnlyDictionary<Skill, int> skills)
    {
        if (skills.Count < Enum.GetValues<Skill>().Length)
        {
            throw new ArgumentException("Skills must be a dictionary with all skills");
        }

        if (skills.Values.Any(v => v is < 0 or > MaxExperience))
        {
            throw new ArgumentException($"Skill experience must be between 0 and {MaxExperience}");
        }
    }
}
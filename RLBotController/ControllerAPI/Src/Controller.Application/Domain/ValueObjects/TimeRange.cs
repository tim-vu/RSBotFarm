using System;
using System.Collections.Generic;
using Controller.SharedKernel;

namespace Controller.Application.Domain.ValueObjects;

public class TimeRange : ValueObject
{
    public TimeOnly Start { get; }
    
    public TimeOnly End { get; }

    public long ScriptId { get; set; }
    
    private TimeRange()
    {
        
    }
    
    public TimeRange(TimeOnly start, TimeOnly end, long scriptId)
    {
        if (start >= end)
        {
            throw new ArgumentException($"{nameof(start)} should be less then {nameof(end)}", nameof(start));
        }
        
        Start = start;
        End = end;
        ScriptId = scriptId;
    }

    public bool Contains(TimeOnly timeOnly)
    {
        return timeOnly >= Start && timeOnly <= End;
    }

    public bool Overlaps(TimeRange other)
    {
        return (Start >= other.Start && Start <= other.End) || (End >= other.Start && End <= other.End);
    }
    
    protected override IEnumerable<object> GetEqualityComponents()
    {
        yield return Start;
        yield return End;
    }

    public override object Clone()
    {
        return new TimeRange(Start, End, ScriptId);
    }
}
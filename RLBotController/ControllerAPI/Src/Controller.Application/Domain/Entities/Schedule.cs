using System;
using System.Collections.Generic;
using System.Linq;
using Controller.Application.Domain.ValueObjects;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public class Schedule : Entity<long>
{
    public bool IsOccupied(DateTimeOffset dateTime)
    {
        return TimeRanges.Any(r => r.Contains(TimeOnly.FromTimeSpan(dateTime.TimeOfDay)));
    }
    
    public IReadOnlyList<TimeRange> TimeRanges => _timeRanges.AsReadOnly();
    
    public void UpdateTimeRanges(ICollection<TimeRange> timeRanges)
    {
        if (timeRanges.Any(r1 => timeRanges.Where(r2 => r2 != r1).Any(r1.Overlaps)))
        {
            throw new ArgumentException($"{nameof(timeRanges)} must not contain overlapping TimeRanges");
        }
        
        _timeRanges = timeRanges.OrderBy(r => r.Start).ToList();
    }

    private List<TimeRange> _timeRanges = new();
}
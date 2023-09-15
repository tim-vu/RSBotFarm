using System;

namespace Controller.Application.Common.ScheduledTask;

[AttributeUsage(AttributeTargets.Class)]
public class ScheduledTaskAttribute : Attribute
{
    public uint IntervalSeconds { get; }

    public ScheduledTaskAttribute(uint intervalSeconds)
    {
        IntervalSeconds = intervalSeconds;
    }
}
using System;

namespace Controller.Application.Common.Services.Clock;

public class Clock : IClock
{
    public DateTimeOffset UtcNow => DateTime.UtcNow;
}
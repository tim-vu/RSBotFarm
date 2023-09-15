using System;

namespace Controller.Application.Common.Services.Clock;

public interface IClock
{
    DateTimeOffset UtcNow { get; }
}
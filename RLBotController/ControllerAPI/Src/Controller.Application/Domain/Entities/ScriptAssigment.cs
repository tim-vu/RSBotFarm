using System;

namespace Controller.Application.Domain.Entities;

public class ScriptAssigment
{
    public long ScriptId { get; private set; }
    
    public DateTimeOffset At { get; private set; }

    public ScriptAssigment(long scriptId, DateTimeOffset at)
    {
        ScriptId = scriptId;
    }
}
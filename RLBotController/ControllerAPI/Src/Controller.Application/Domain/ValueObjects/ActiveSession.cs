using System;
using System.Collections.Generic;
using Controller.SharedKernel;

namespace Controller.Application.Domain.ValueObjects;

public class ActiveSession : ValueObject
{
    public long ScriptId { get; private set; }
    
    public long AccountId { get; private set; }
    
    public DateTimeOffset StartedAt { get; private set; }

    public ActiveSession(long scriptId, long accountId, DateTimeOffset startedAt)
    {
        ScriptId = scriptId;
        AccountId = accountId;
        StartedAt = startedAt;
    }

    protected override IEnumerable<object> GetEqualityComponents()
    {
        yield return ScriptId;
        yield return AccountId;
    }

    public override object Clone()
    {
        return new ActiveSession(ScriptId, AccountId, StartedAt);
    }
}
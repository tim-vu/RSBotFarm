using Controller.Application.Domain.Entities;
using Controller.SharedKernel;
using Microsoft.AspNetCore.Mvc.TagHelpers;

namespace Controller.Application.Domain.Events;

public class ScriptCreatedEvent : BaseDomainEvent
{
    public Script Script { get; }

    public ScriptCreatedEvent(Script script)
    {
        Script = script;
    }
}
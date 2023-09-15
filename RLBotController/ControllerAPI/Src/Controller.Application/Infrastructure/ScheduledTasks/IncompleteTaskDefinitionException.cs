using System;
using Controller.Application.Common.ScheduledTask;

namespace Controller.Application.Infrastructure.ScheduledTasks;

public class IncompleteTaskDefinitionException : Exception
{
    public Type Type { get; set; }

    public IncompleteTaskDefinitionException(Type type) : base($"The task {type.Name} is missing its {nameof(ScheduledTaskAttribute)} attribute")
    {
        Type = type;
    }
}
using System.Collections.Generic;

namespace Controller.SharedKernel;

public abstract class Entity<TId> where TId : struct
{
    public TId Id { get; private set; }
    
    public IReadOnlyCollection<BaseDomainEvent> Events => _events.AsReadOnly();

    public void AddEvent(BaseDomainEvent @event)
    {
        _events.Add(@event);
    }

    public void ClearEvents()
    {
        _events.Clear();
    }
    
    private readonly List<BaseDomainEvent> _events = new();
}
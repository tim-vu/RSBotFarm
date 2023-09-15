using MediatR;

namespace Controller.SharedKernel;

public interface IDomainEventHandler<in TEvent> : INotificationHandler<TEvent> where TEvent : BaseDomainEvent
{
    
}
using MediatR;

namespace Controller.Application.Common.Abstractions.Messaging;

public interface IQuery<out T> : IRequest<T>
{
    
}
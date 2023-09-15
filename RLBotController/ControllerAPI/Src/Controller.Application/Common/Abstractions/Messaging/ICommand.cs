using MediatR;

namespace Controller.Application.Common.Abstractions.Messaging;

public interface IBaseCommand
{
    
}

public interface ICommand<out T> : IRequest<T>, IBaseCommand
{
    
}

public interface ICommand : IRequest, IBaseCommand
{
    
}
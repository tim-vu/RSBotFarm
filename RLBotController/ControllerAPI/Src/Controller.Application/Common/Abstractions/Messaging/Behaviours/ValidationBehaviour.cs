using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using FluentValidation;
using MediatR;

namespace Controller.Application.Common.Abstractions.Messaging.Behaviours;

public class ValidationBehaviour<TRequest, TResponse> : 
    IPipelineBehavior<TRequest, TResponse> where TRequest : IValidatedCommand<TResponse>
{
    private readonly IEnumerable<IValidator<TRequest>> _validators;

    public ValidationBehaviour(IEnumerable<IValidator<TRequest>> validators)
    {
        _validators = validators;
    }

    public Task<TResponse> Handle(TRequest request, RequestHandlerDelegate<TResponse> next, CancellationToken cancellationToken)
    {
        throw new System.NotImplementedException();
    }
}

public interface IValidatedCommand<out T> : IBaseCommand, IRequest<T>
{
         
}
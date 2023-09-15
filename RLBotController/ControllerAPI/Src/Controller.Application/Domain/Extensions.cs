using Controller.Application.Domain.Factories.AccountFactory;
using Microsoft.Extensions.DependencyInjection;

namespace Controller.Application.Domain;

public static class Extensions
{
       public static IServiceCollection AddDomain(this IServiceCollection services)
       {
              services.AddTransient<IAccountFactory, AccountFactory>();
              return services;
       } 
}
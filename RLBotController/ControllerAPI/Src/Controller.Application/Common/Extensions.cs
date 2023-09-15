using Controller.Application.Common.Configuration;
using Controller.Application.Common.Services.ClientContext;
using Controller.Application.Common.Services.Clock;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace Controller.Application.Common;

public static class Extensions
{
    public static IServiceCollection AddCommon(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddTransient<IClock, Clock>();
        services.AddScoped<IClientContext, ClientContext>();
        services.AddHttpContextAccessor();
        services.Configure<AppOptions>(configuration);
        return services;
    }
}
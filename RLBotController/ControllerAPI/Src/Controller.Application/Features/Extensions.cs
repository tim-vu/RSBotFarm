using Controller.Application.Features.Allocations.Accounts;
using Controller.Application.Features.Allocations.Clients;
using Controller.Application.Features.Allocations.ResourceAllocator;
using Controller.Application.Features.Clients.Services.ClientManager;
using Controller.Application.Features.Clients.Services.ClientManager.DockerClientManager;
using Controller.Application.Features.Clients.Tasks.ClientDirector.ClientListSynchronizer;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace Controller.Application.Features;

public static class Extensions
{
    public static void AddFeatures(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddTransient<IAccountAllocator, AccountAllocator>();
        services.AddTransient<IClientAllocator, ClientAllocator>();
        services.AddTransient<IResourceAllocator, ResourceAllocator>();
        services.AddTransient<IClientListSynchronizer, ClientListSynchronizer>();
        
        services.Configure<DockerClientManagerOptions>(configuration.GetSection("DockerClientManager"));
        services.AddTransient<IClientManager, DockerClientManager>();
    }
}
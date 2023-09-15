using System;
using System.Linq;
using System.Reflection;
using Controller.Application.Common.Api;
using Controller.Application.Common.Interfaces;
using Controller.Application.Common.Interfaces.DockerClient;
using Controller.Application.Common.ScheduledTask;
using Controller.Application.Infrastructure.Docker;
using Controller.Application.Infrastructure.NSwag;
using Controller.Application.Infrastructure.Persistence;
using Controller.Application.Infrastructure.ScheduledTasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using NSwag;
using Quartz;

namespace Controller.Application.Infrastructure;

public static class Extensions
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {
        services.Configure<DockerClientOptions>(configuration.GetSection("DockerClient"));
        services.AddTransient<IDockerClient, DockerClient>();
        services.AddTransient<IServerSentEventService, ServerSentEventService.ServerSentEventService>();
        
        services.AddDbContext<RSBControllerContext>(opt =>
        {
            opt.UseNpgsql(configuration.GetConnectionString("RSControllerDb"));
        });

        services.AddScheduledJobs();

        return services;
    }

    private static IServiceCollection AddScheduledJobs(this IServiceCollection services)
    {
        services.AddQuartz(q =>
        {
            q.UseMicrosoftDependencyInjectionJobFactory();

            var targetType = typeof(IScheduledTask);
            var types = Assembly.GetExecutingAssembly()
                .GetTypes()
                .Where(t => targetType.IsAssignableFrom(t) && !t.IsInterface && !t.IsAbstract && !t.IsGenericTypeDefinition)
                .ToList();
            
            var wrapperType = typeof(ScheduledTaskWrapper<>);

            foreach (var type in types)
            {
                var attribute = (ScheduledTaskAttribute?)Attribute.GetCustomAttribute(type, typeof(ScheduledTaskAttribute));

                if (attribute == null)
                {
                    throw new IncompleteTaskDefinitionException(type);
                }

                var typeArgs = new[] { type };
                var jobType = wrapperType.MakeGenericType(typeArgs);
                
                var jobKey = new JobKey(type.Name);
                q.AddJob(jobType, jobKey, opt =>
                {
                    opt.DisallowConcurrentExecution();
                });

                services.AddTransient(type);

                q.AddTrigger(opts =>
                {
                    opts.ForJob(jobKey)
                        .WithIdentity(jobKey.Name + "-trigger")
                        .WithSimpleSchedule(a =>
                        {
                            a.WithInterval(TimeSpan.FromSeconds(attribute.IntervalSeconds));
                            a.RepeatForever();
                        });
                });
            }
        });

        services.AddQuartzHostedService(q =>
        {
            q.AwaitApplicationStarted = true;
            q.WaitForJobsToComplete = true;
        });
        return services;
    }

    public static IServiceCollection AddSwagger(this IServiceCollection services)
    {
        services.AddSwaggerDocument(opt =>
        {
            opt.Title = "RSController Dashboard API";
            opt.DocumentName = "Dashboard";
            opt.SchemaNameGenerator = new SchemaNameGenerator();
            opt.OperationProcessors.Insert(0, new RouteGroupFilter(RouteGroups.Dashboard));
            opt.OperationProcessors.Add(new TagBasedOnRouteProcessor());
            opt.GenerateAbstractSchemas = false;
        });
        
        services.AddSwaggerDocument(opt =>
        {
            opt.Title = "RSController Client API";
            opt.DocumentName = "Clients";
            opt.SchemaNameGenerator = new SchemaNameGenerator();
            opt.OperationProcessors.Insert(0, new RouteGroupFilter(RouteGroups.Clients));
            opt.OperationProcessors.Add(new TagBasedOnRouteProcessor());
            opt.GenerateAbstractSchemas = false;
            opt.AddSecurity("ApiKey", Enumerable.Empty<string>(), new OpenApiSecurityScheme
            {
                Type = OpenApiSecuritySchemeType.ApiKey,
                Name = "BotId",
                In = OpenApiSecurityApiKeyLocation.Header,
            });
        });
        
        services.AddSwaggerDocument(opt =>
        {
            opt.Title = "RSController Client API";
            opt.DocumentName = "Mules";
            opt.SchemaNameGenerator = new SchemaNameGenerator();
            opt.OperationProcessors.Insert(0, new RouteGroupFilter(RouteGroups.Mules));
            opt.OperationProcessors.Add(new TagBasedOnRouteProcessor());
            opt.GenerateAbstractSchemas = false;
            opt.AddSecurity("ApiKey", Enumerable.Empty<string>(), new OpenApiSecurityScheme
            {
                Type = OpenApiSecuritySchemeType.ApiKey,
                Name = "BotId",
                In = OpenApiSecurityApiKeyLocation.Header,
            });
        });
        
        services.AddSwaggerDocument(opt =>
        {
            opt.Title = "RSController Client API";
            opt.DocumentName = "Workers";
            opt.SchemaNameGenerator = new SchemaNameGenerator();
            opt.OperationProcessors.Insert(0, new RouteGroupFilter(RouteGroups.Workers));
            opt.OperationProcessors.Add(new TagBasedOnRouteProcessor());
            opt.GenerateAbstractSchemas = false;
            opt.AddSecurity("ApiKey", Enumerable.Empty<string>(), new OpenApiSecurityScheme
            {
                Type = OpenApiSecuritySchemeType.ApiKey,
                Name = "BotId",
                In = OpenApiSecurityApiKeyLocation.Header,
            });
        });

        return services;
    }

    public static void UseSwagger(this IApplicationBuilder app, IConfiguration configuration)
    {
        app.UseOpenApi(opt => opt.PostProcess = (d, _) =>
        {
            d.Host = configuration["Host"];
        });
        app.UseSwaggerUi3();
    }
}
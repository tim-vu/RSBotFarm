using System;
using System.Text.Json.Serialization;
using AspNetCore.Authentication.ApiKey;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Controller.Application.Common.Authorization;
using Controller.Application.Domain;
using Controller.Application.Features;
using Controller.Application.Infrastructure;
using Lib.AspNetCore.ServerSentEvents;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

namespace Controller.Application;

public class Startup
{
    private IConfiguration Configuration { get; }

    public Startup(IConfiguration configuration)
    {
        Configuration = configuration;
    }
    
    public void ConfigureServices(IServiceCollection services)
    {
        services.AddMediatR(opt =>
        {
            opt.RegisterServicesFromAssemblyContaining<Startup>();
        });

        services.AddCommon(Configuration);
        services.AddDomain();
        services.AddFeatures(Configuration);
        services.AddInfrastructure(Configuration);

        services.AddSwagger();

        services.AddCors(opt =>
        {
            opt.AddPolicy("AllowLocalhost", builder =>
            {
                builder.SetIsOriginAllowed(origin => new Uri(origin).Host == "localhost")
                    .AllowAnyHeader()
                    .AllowAnyMethod()
                    .AllowCredentials();
            });
        });

        services.AddAuthentication(ApiKeyDefaults.AuthenticationScheme)
            .AddApiKeyInHeader<ApiKeyProvider>(opt =>
            {
                opt.KeyName = "BotId";
                opt.Realm = "RSBController";
            });
        
        services.AddServerSentEvents();
        services.AddControllers(opt =>
        {
            opt.Conventions.Add(new PrefixRouteGroupConvention());
        }).AddJsonOptions(options =>
        {
            options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter());
        });
    }

    public void Configure(IApplicationBuilder app, IWebHostEnvironment environment)
    {
        if (environment.IsDevelopment())
        {
            app.UseDeveloperExceptionPage();
            app.UseSwagger(Configuration);
        }   
        else
        {
            app.UseHsts();
        }

        app.UseHttpsRedirection();
        
        app.UseCors("AllowLocalhost");
        
        app.UseRouting();

        app.UseAuthentication();
        app.UseAuthorization();
        
        app.UseEndpoints(endpoints =>
        {
            endpoints.MapServerSentEvents("/dashboard/updates", new ServerSentEventsOptions
            {
                RequireAcceptHeader = true
            });
            endpoints.MapControllers();
        });
    }
}

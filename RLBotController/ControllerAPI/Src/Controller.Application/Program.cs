using Controller.Application;
using Controller.Application.Infrastructure.Persistence;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using RSBController.Application;
using RSBController.Application.Infrastructure.Persistence;

var builder = Host.CreateDefaultBuilder()
    .ConfigureWebHostDefaults(builder =>
    {
        builder.UseStartup<Startup>();
    });
    
var host = builder.Build();

await using (var scope = host.Services.CreateAsyncScope())
{
    var context = scope.ServiceProvider.GetRequiredService<RSBControllerContext>();
    await context.Database.MigrateAsync();
};

await host.RunAsync();
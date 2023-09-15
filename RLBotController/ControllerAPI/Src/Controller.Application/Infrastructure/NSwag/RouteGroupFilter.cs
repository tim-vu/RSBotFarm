using System.Reflection;
using Controller.Application.Common.Api;
using NSwag.Generation.Processors;
using NSwag.Generation.Processors.Contexts;

namespace Controller.Application.Infrastructure.NSwag;

public class RouteGroupFilter : IOperationProcessor
{
    private readonly string _routeGroup;

    public RouteGroupFilter(string routeGroup)
    {
        _routeGroup = routeGroup;
    }
    
    public bool Process(OperationProcessorContext context)
    {
        var routeGroup = context.ControllerType.GetCustomAttribute<RouteGroupAttribute>()?.Group;
        return routeGroup != null && routeGroup == _routeGroup;
    }
}
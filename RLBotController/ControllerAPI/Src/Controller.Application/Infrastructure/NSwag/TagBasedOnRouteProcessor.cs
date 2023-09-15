using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.AspNetCore.Mvc;
using NSwag.Generation.Processors;
using NSwag.Generation.Processors.Contexts;

namespace Controller.Application.Infrastructure.NSwag;

public class TagBasedOnRouteProcessor : IOperationProcessor
{
    public bool Process(OperationProcessorContext context)
    {
        var controllerType = context.MethodInfo.DeclaringType;

        if (controllerType == null)
        {
            return true;
        }

        var routeAttribute = (RouteAttribute?)Attribute.GetCustomAttribute(controllerType, typeof(RouteAttribute));

        if (routeAttribute == null)
        {
            return true;
        }

        var lastPathSegment = FirstCharToUpper(routeAttribute.Template.Split("/").Last());
        
        context.OperationDescription.Operation.Tags = new List<string> { lastPathSegment };
        
        context.OperationDescription.Operation.OperationId = $"{lastPathSegment}_{context.MethodInfo.Name}";
        return true;
    }

    private static string FirstCharToUpper(string input)
    {
        return input[0].ToString().ToUpper() + input[1..];
    }
}
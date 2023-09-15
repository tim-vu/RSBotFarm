using System;

namespace Controller.Application.Common.Api;

[AttributeUsage(AttributeTargets.Class)]
public class RouteGroupAttribute : Attribute
{
    public string Group { get; }

    public RouteGroupAttribute(string group)
    {
        Group = group;
    }
}
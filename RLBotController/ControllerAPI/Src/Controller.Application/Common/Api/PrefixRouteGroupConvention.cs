using System.Linq;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ApplicationModels;

namespace Controller.Application.Common.Api;

public class PrefixRouteGroupConvention : IControllerModelConvention
{
    public void Apply(ControllerModel controller)
    {
        var attribute = (RouteGroupAttribute?)controller.Attributes.FirstOrDefault(a => a is RouteGroupAttribute);

        if (attribute == null)
        {
            return;
        }

        var routePrefix = new AttributeRouteModel(new RouteAttribute(attribute.Group.ToLowerInvariant()));

        foreach (var selector in controller.Selectors)
        {
            if (selector.AttributeRouteModel == null)
            {
                selector.AttributeRouteModel = routePrefix;
                continue;
            }
            
            selector.AttributeRouteModel = AttributeRouteModel.CombineAttributeRouteModel(routePrefix, selector.AttributeRouteModel);
        }
    }
}
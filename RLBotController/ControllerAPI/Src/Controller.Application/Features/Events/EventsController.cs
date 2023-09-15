using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Controller.Application.Features.Events.Models;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Events;

[Route("api/events")]
[RouteGroup(RouteGroups.Dashboard)]
public class EventsController : AbstractController
{
    [HttpGet]
    public async Task<ActionResult<EventVm[]>> GetAllEvents() => await Mediator.Send(new GetEvents.Query());
}
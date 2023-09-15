using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Clients;

[Route("api/clients")]
[RouteGroup(RouteGroups.Clients)]
[Authorize]
public class ClientClientsController : AbstractController
{
    [HttpGet("activesession")]
    public async Task<ActionResult<GetActiveSession.ActiveSessionVm>> GetActiveSession()
    {
        return Ok(await Mediator.Send(new GetActiveSession.Query()));
    }
    
    [HttpPost("heartbeat")]
    public async Task<IActionResult> SendHeartbeat()
    {
        await Mediator.Send(new SendHeartbeat.Command());
        return NoContent();
    }
    
    [HttpPost("stop")]
    public async Task<IActionResult> SignalStop()
    {
        await Mediator.Send(new SignalStop.Command());
        return NoContent();
    }
}
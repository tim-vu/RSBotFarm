using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Muling;

[Route("api/muling")]
[RouteGroup(RouteGroups.Workers)]
[Authorize]
public class WorkersMulingController : AbstractController
{
    [HttpPost("requests")]
    public async Task<ActionResult<CreateMulingRequest.MulingRequestVm>> CreateMulingRequest(
        [FromBody] CreateMulingRequest.Command command)
    {
        return Ok(await Mediator.Send(command));
    }

    [HttpPost("requests/{id:long}/complete")]
    public async Task<IActionResult> CompleteMulingRequest([FromRoute] long id)
    {
        await Mediator.Send(new CompleteMuling.Command(id));
        return NoContent();
    }
}   
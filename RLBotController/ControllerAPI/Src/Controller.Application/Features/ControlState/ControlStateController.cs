using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Controller.Application.Features.ControlState.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.ControlState;

[Route("api/controlstate")]
[RouteGroup(RouteGroups.Dashboard)]
public class ControlStateController : AbstractController
{
    [HttpGet]
    public async Task<ActionResult<ControlStateVm>> Get()
    {
        return await Mediator.Send(new GetControlState.Query());
    }

    [HttpPut]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    public async Task<IActionResult> Update([FromBody] UpdateControlState.Command command)
    {
        await Mediator.Send(command);
        return NoContent();
    }
}
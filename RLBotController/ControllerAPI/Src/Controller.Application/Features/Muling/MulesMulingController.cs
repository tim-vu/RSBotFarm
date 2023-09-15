using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Muling;

[Route("api/mule")]
[RouteGroup(RouteGroups.Mules)]
[Authorize]
public class MulesMulingController : AbstractController
{
    [HttpPost]
    public async Task<ActionResult> CreateMule([FromBody] CreateMule.Command command)
    {
        await Mediator.Send(command);
        return NoContent();
    }

    [HttpGet("requests")]
    public async Task<ActionResult<GetActiveMulingRequests.ActiveMulingRequestVm[]>> GetMulingRequests()
    {
        return Ok(await Mediator.Send(new GetActiveMulingRequests.Query()));
    }
}
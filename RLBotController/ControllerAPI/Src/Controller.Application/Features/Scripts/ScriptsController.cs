using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Controller.Application.Domain.Factories.AccountFactory;
using Controller.Application.Features.Scripts.Models;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Scripts;

[Route("api/scripts")]
[RouteGroup(RouteGroups.Dashboard)]
public class ScriptsController : AbstractController
{
    [HttpGet]
    public async Task<ActionResult<ScriptVm[]>> GetAll() => await Mediator.Send(new GetScripts.Query());
    
    [HttpPost]
    public async Task<IActionResult> CreateScript([FromBody] AddScript.Command command)
    {
        await Mediator.Send(command);
        return Ok();
    }
}
using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Accounts.Mules;

[Route("api/accounts/mules")]
[RouteGroup(RouteGroups.Dashboard)]
public class MuleAccountsController : AbstractController
{
    [HttpGet]
    public async Task<ActionResult<GetMules.MuleAccountVm>> GetAll()
    {
        return Ok(await Mediator.Send(new GetMules.Query()));
    }

    [HttpPost]
    public async Task<IActionResult> AddMuleAccount([FromBody] AddMuleAccount.Command command)
    {
        await Mediator.Send(command);
        return NoContent();
    }
}
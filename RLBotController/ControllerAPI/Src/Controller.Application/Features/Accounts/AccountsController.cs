using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Accounts;

[Route("api/accounts")]
[RouteGroup(RouteGroups.Clients)]
public class AccountsController : AbstractController
{
    [HttpPatch("{accountId}")]
    public async Task<IActionResult> UpdateAccount([FromRoute] long accountId, [FromBody] UpdateAccount.Command command)
    {
        command.Id = accountId;
        await Mediator.Send(command);
        return NoContent();
    }

    [HttpPatch("{accountId}/playerdetails")]
    public async Task<IActionResult> UpdatePlayerDetails([FromRoute] long accountId, [FromBody] UpdatePlayerDetails.Command command)
    {
        command.Id = accountId;
        await Mediator.Send(command);
        return NoContent();
    }
}

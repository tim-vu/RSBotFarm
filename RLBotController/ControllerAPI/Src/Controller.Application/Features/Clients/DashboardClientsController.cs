using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Controller.Application.Features.Clients.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Clients;

[Route("api/clients")]
[RouteGroup(RouteGroups.Dashboard)]
[AllowAnonymous]
public class DashboardClientsController : AbstractController
{
    [HttpGet]
    public async Task<ActionResult<ClientVm[]>> GetAll() => await Mediator.Send(new GetClients.Query());
}
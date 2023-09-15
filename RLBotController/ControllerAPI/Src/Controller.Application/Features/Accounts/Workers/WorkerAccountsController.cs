using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Threading.Tasks;
using Controller.Application.Common;
using Controller.Application.Common.Api;
using Controller.Application.Features.Accounts.Workers.Models;
using CsvHelper;
using CsvHelper.Configuration;
using Microsoft.AspNetCore.Mvc;

namespace Controller.Application.Features.Accounts.Workers;

[Route("api/accounts/workers")]
[RouteGroup(RouteGroups.Dashboard)]
public class WorkerAccountsController : AbstractController
{
    [HttpGet]
    public async Task<ActionResult<WorkerAccountVm[]>> GetAll() => await Mediator.Send(new GetWorkerAccounts.Query());

    [HttpPost]
    public async Task<ActionResult<WorkerAccountVm>> Create([FromBody] AddWorkerAccount.Command command) =>await Mediator.Send(command);

    private static readonly CsvConfiguration CsvConfiguration = new(CultureInfo.InvariantCulture)
    {
        NewLine = Environment.NewLine,
        HasHeaderRecord = false
    };
    
    [HttpPatch]
    [Consumes("text/csv")]
    public async Task<IActionResult> BulkCreate()
    {
        using var reader = new StreamReader(Request.BodyReader.AsStream());
        using var csv = new CsvReader(reader, CsvConfiguration);
        csv.Context.AutoMap<BulkAddWorkerAccount.Worker>();
        
        var accounts = new List<BulkAddWorkerAccount.Worker>();
        await foreach (var account in csv.GetRecordsAsync<BulkAddWorkerAccount.Worker>())
        {
            accounts.Add(account);  
        }

        await Mediator.Send(new BulkAddWorkerAccount.Command(accounts.ToArray()));
        return NoContent();
    }
}
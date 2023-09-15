using System.Threading;
using System.Threading.Tasks;
using Controller.Application.Common.Interfaces;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.Events;
using Controller.Application.Features.Clients.Models;
using Controller.Application.Infrastructure.Persistence;
using Controller.SharedKernel;

namespace Controller.Application.Features.Clients.Handlers;

public class ClientStatusChangedEventHandler : IDomainEventHandler<ClientStatusChangedEvent>
{
    private readonly RSBControllerContext _context;
    private readonly IServerSentEventService _serverSentEventService;

    public ClientStatusChangedEventHandler(RSBControllerContext context, IServerSentEventService serverSentEventService)
    {
        _context = context;
        _serverSentEventService = serverSentEventService;
    }

    public async Task Handle(ClientStatusChangedEvent notification, CancellationToken cancellationToken)
    {
        var script = await _context.Scripts.FindAsync(notification.Client.ActiveSession.ScriptId);

        if (notification.Client.Status == ClientStatus.Stopped)
        {
            await _serverSentEventService.Send("ClientVmRemoved", notification.Client.Id);
            return;
        }
        
        var clientVm = new ClientVm(
            notification.Client.Id,
            notification.Client.BotId,
            notification.Client.Status,
            notification.Client.StartedAt?.ToUnixTimeSeconds(),
            new ActiveScriptVm(script!.Id, script.Name),
            notification.Client.WebsocketPort
        );
        
        await _serverSentEventService.Send(clientVm);
    }
}
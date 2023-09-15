using Controller.Application.Domain.Enums;

namespace Controller.Application.Features.Clients.Models;

public sealed record ClientVm(
    long Id,
    string BotId,
    ClientStatus Status,
    long? StartedAt,
    ActiveScriptVm? ActiveScript,
    int? WebsocketPort
);

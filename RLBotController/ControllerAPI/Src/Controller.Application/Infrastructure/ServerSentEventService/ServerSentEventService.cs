using System.Collections.Generic;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using Controller.Application.Common.Interfaces;
using Lib.AspNetCore.ServerSentEvents;

namespace Controller.Application.Infrastructure.ServerSentEventService;

public class ServerSentEventService : IServerSentEventService
{
    private static readonly JsonSerializerOptions JsonSerializerOptions = new()
    {
        PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
        Converters = { new JsonStringEnumConverter() }
    };
    
    private readonly IServerSentEventsService _serverSentEventsService;

    public ServerSentEventService(IServerSentEventsService serverSentEventsService)
    {
        _serverSentEventsService = serverSentEventsService;
    }

    public Task Send<T>(T data)
    {
        return Send(typeof(T).Name, data);
    }

    public Task Send<T>(string evenType, T data)
    {
        return _serverSentEventsService.SendEventAsync(new ServerSentEvent
        {
            Type = evenType,
            Data = new List<string> { JsonSerializer.Serialize(data, JsonSerializerOptions) }
        });
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Controller.Application.Common.Configuration;
using Controller.Application.Common.Interfaces.DockerClient;
using Controller.Application.Common.Interfaces.DockerClient.Models;
using Controller.Application.Domain.Entities;
using Microsoft.Extensions.Options;

namespace Controller.Application.Features.Clients.Services.ClientManager.DockerClientManager;

public class DockerClientManager : IClientManager
{
    private const string BotIdLabelKey = "BotId";
    
    private readonly IDockerClient _dockerClient;
    private readonly AppOptions _appOptions;
    private readonly DockerClientManagerOptions _options;

    public DockerClientManager(
        IDockerClient dockerClient,
        IOptions<AppOptions> appOptions,
        IOptions<DockerClientManagerOptions> options)
    {
        _dockerClient = dockerClient;
        _appOptions = appOptions.Value;
        _options = options.Value;
    }

    public async Task<List<RunningClientDto>> GetClients()
    {
        var containers = await _dockerClient.GetContainers();
        return containers
            .Where(c => c.ImageName == _options.ImageName)
            .Where(c => c.Labels.ContainsKey(BotIdLabelKey))
            .Select(c => new RunningClientDto(c.Labels[BotIdLabelKey]))
            .ToList();
    }

    public async Task<StartedClientDto> LaunchClient(Client client, Script script, Account account)
    {
        var containerId= await StartContainer(client);

        if (containerId == null)
        {
            throw new ApplicationException($"Unable to start container for BotId {client.BotId}");
        }

        var details = await _dockerClient.InspectContainer(containerId);

        var vncPort = details.PortMapping.Single(p => p.Value == _options.InternalVncPort).Key;
        var websocketPort = details.PortMapping.Single(p => p.Value == _options.InternalWsPort).Key;

        return new StartedClientDto(vncPort, websocketPort);
    }

    private async Task<string?> StartContainer(Client client)
    {
        var domain = _appOptions.Host[.._appOptions.Host.IndexOf(":", StringComparison.Ordinal)];
        
        var container = await _dockerClient.CreateContainer(new CreateContainerDto
        {
            Image = _options.ImageName,
            Name = null,
            Binds = _options.Binds,
            ExtraHosts = new List<string>
            {
                $"{domain}:{_options.HostOverride}"
            },
            Labels = new Dictionary<string, string>
            {
                {BotIdLabelKey, client.BotId}
            },
            PortMapping = new Dictionary<int, List<int>>
            {
                { 0, new List<int> { _options.InternalVncPort, _options.InternalWsPort} }
            },
            Environment = new List<string> { $"BOT_ID={client.BotId}"},
            NetworkMode = _options.NetworkMode
        });

        if (!await _dockerClient.StartContainer(container.ContainerId))
        {
            return null;
        }

        return container.ContainerId;
    }

    public async Task StopClient(string processId)
    {
        await _dockerClient.StopContainer(processId);
    }
}
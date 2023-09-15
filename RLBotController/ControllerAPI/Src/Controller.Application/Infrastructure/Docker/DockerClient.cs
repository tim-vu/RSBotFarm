using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Controller.Application.Common.Interfaces.DockerClient.Models;
using Docker.DotNet;
using Docker.DotNet.Models;
using Microsoft.Extensions.Options;

namespace Controller.Application.Infrastructure.Docker;

public class DockerClient : Common.Interfaces.DockerClient.IDockerClient
{
    private readonly global::Docker.DotNet.DockerClient _client;

    public DockerClient(IOptions<DockerClientOptions> options)
    {
        _client = new DockerClientConfiguration(new Uri(options.Value.Url))
            .CreateClient();
    }

    public async Task<List<ContainerDto>> GetContainers()
    {
        try
        {
            var response = await _client.Containers.ListContainersAsync(new ContainersListParameters());
            
            return response.Select(c => new ContainerDto
            {
                ContainerId = c.ID,
                ImageName = c.Image,
                Labels = new Dictionary<string, string>(c.Labels)
            }).ToList();
        }
        catch (Exception ex)
        {
            throw new ApplicationException("An exception occured while fetching containers", ex);
        }
    }

    public async Task<CreateContainerResponseDto> CreateContainer(CreateContainerDto createContainerDto)
    {
        try
        {
            var parameters = new CreateContainerParameters
            {
                Image = createContainerDto.Image,
                HostConfig = new HostConfig
                {
                    Binds = createContainerDto.Binds,
                    ExtraHosts = createContainerDto.ExtraHosts,
                    Memory = 524288000,
                    NetworkMode = createContainerDto.NetworkMode,
                },
                Cmd = createContainerDto.Cmd,
                Labels = createContainerDto.Labels,
                Env = createContainerDto.Environment
            };

            if (createContainerDto.PortMapping != null)
            {
                parameters.ExposedPorts = createContainerDto.PortMapping
                    .Values
                    .SelectMany(v => v)
                    .ToDictionary(v => $"{v}/tcp", _ => new EmptyStruct());

                parameters.HostConfig.PortBindings = createContainerDto.PortMapping
                    .SelectMany(p => p.Value.Select(i => (HostPort: p.Key, InternalPort: i)))
                    .ToDictionary(p => $"{p.InternalPort}/tcp", p => (IList<PortBinding>)new List<PortBinding>
                    {
                        new()
                        {
                            HostPort = p.HostPort.ToString()
                        }
                    });
            }

            var response = await _client.Containers.CreateContainerAsync(parameters);

            return new CreateContainerResponseDto
            {
                ContainerId = response.ID
            };
        }
        catch (Exception ex)
        {
            throw new ApplicationException("An exception occurred while creating a container", ex);
        }
    }

    public async Task<InspectContainerResponseDto> InspectContainer(string containerId)
    {
        try
        {
            var response = await _client.Containers.InspectContainerAsync(containerId);

            var portMapping = new Dictionary<int, int>();

            foreach (var mapping in response.NetworkSettings.Ports)
            {
                var internalPort = int.Parse(mapping.Key.Split("/")[0]);
                var hostPort = mapping.Value.Select(p => int.Parse(p.HostPort)).FirstOrDefault();
                
                portMapping.Add(hostPort, internalPort);
            }
            
            return new InspectContainerResponseDto
            {
                ContainerId = response.ID,
                PortMapping = portMapping
            };
        }
        catch (Exception ex)
        {
            throw new ApplicationException("An exception occurred while inspecting a container", ex);
        }
    }

    public Task<bool> StartContainer(string containerId)
    {
        try
        {
            return _client.Containers.StartContainerAsync(containerId, new ContainerStartParameters());
        }
        catch (Exception ex)
        {
            throw new ApplicationException("An exception occurred while starting a container", ex);
        }
    }

    public Task<bool> StopContainer(string containerId)
    {
        try
        {
            return _client.Containers.StopContainerAsync(containerId, new ContainerStopParameters());
        }
        catch (Exception ex)
        {
            throw new ApplicationException("An exception occurred while stopping a container", ex);
        }
    }
}
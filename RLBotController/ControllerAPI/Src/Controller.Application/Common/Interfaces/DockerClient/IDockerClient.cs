using System.Collections.Generic;
using System.Threading.Tasks;
using Controller.Application.Common.Interfaces.DockerClient.Models;

namespace Controller.Application.Common.Interfaces.DockerClient;

public interface IDockerClient
{
    Task<List<ContainerDto>> GetContainers();

    Task<CreateContainerResponseDto> CreateContainer(CreateContainerDto createContainerDto);

    Task<InspectContainerResponseDto> InspectContainer(string containerId);

    Task<bool> StartContainer(string containerId);

    Task<bool> StopContainer(string containerId);
}
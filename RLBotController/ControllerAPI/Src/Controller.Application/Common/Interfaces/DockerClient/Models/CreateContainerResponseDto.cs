namespace Controller.Application.Common.Interfaces.DockerClient.Models;

public class CreateContainerResponseDto
{
    public required string ContainerId { get; init; }
}
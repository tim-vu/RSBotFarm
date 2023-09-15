using System.Collections.Generic;

namespace Controller.Application.Common.Interfaces.DockerClient.Models;

public class ContainerDto
{
    public required string ContainerId { get; set; }
    
    public required string ImageName { get; set; }
    
    public required IReadOnlyDictionary<string, string> Labels { get; set; }
}
using System.Collections.Generic;

namespace Controller.Application.Common.Interfaces.DockerClient.Models;

public class InspectContainerResponseDto
{
    public required string ContainerId { get; set; }
    
    public required Dictionary<int, int> PortMapping { get; set; }
}
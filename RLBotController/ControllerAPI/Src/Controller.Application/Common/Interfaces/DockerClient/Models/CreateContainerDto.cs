using System.Collections.Generic;

namespace Controller.Application.Common.Interfaces.DockerClient.Models;

public class CreateContainerDto
{
    public required string? Name { get; set; }
    
    public required string Image { get; set; }

    public List<string>? Binds { get; set; }
    
    public List<string>? Cmd { get; set; }
    
    public List<string>? ExtraHosts { get; set; }
    
    public Dictionary<string, string>? Labels { get; set; }

    public Dictionary<int, List<int>>? PortMapping { get; set; }
    
    public string? NetworkMode { get; set; }
    
    public List<string>? Environment { get; set; }
}
using System.Collections.Generic;

namespace Controller.Application.Features.Clients.Services.ClientManager.DockerClientManager;

public class DockerClientManagerOptions
{
    public required string ImageName { get; set; }
    
    public required List<string> Binds { get; set; }
    
    public int InternalVncPort { get; set; }
    
    public int InternalWsPort { get; set; }
    
    public required string HostOverride { get; set; }
    
    public string? NetworkMode { get; set; }
}
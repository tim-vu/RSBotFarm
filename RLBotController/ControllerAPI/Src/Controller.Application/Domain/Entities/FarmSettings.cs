using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public class FarmSettings : Entity<long>
{
    public int MinimumMuleCount { get; set; }
    
    public int MinimumWorkerCount { get; set; }
}
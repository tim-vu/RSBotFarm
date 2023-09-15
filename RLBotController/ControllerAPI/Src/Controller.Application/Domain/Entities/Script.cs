using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public class Script : Entity<long>
{
    public string Name { get; private set; }
    
    public int Instances { get; set; }
    
    public int Priority { get; protected set; }

    public Script(string name, int instances, int priority)
    {
        Name = name;
        Instances = instances;
        Priority = priority;
    }
}
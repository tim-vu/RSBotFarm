namespace Controller.Application.Domain.Entities;

public class WorkerScript : Script
{
    public new int Priority
    {
        get => base.Priority;
        set => base.Priority = value;
    }
    
    public WorkerScript(string name, int instances, int priority) : base(name, instances, priority)
    {
    }
}
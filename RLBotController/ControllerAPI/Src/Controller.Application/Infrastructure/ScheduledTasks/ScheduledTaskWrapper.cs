using System.Threading.Tasks;
using Controller.Application.Common.ScheduledTask;
using Quartz;

namespace Controller.Application.Infrastructure.ScheduledTasks;

public class ScheduledTaskWrapper<T> : IJob where T : IScheduledTask
{
    private readonly T _task;
    
    public ScheduledTaskWrapper(T task)
    {
        _task = task;
    }

    public Task Execute(IJobExecutionContext context)
    {
        if (context.CancellationToken.IsCancellationRequested)
        {
            return Task.CompletedTask;
        }
        
        return _task.DoExecute();
    }
}
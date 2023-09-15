using System.Threading.Tasks;

namespace Controller.Application.Common.ScheduledTask;

public interface IScheduledTask
{
    Task DoExecute();
}
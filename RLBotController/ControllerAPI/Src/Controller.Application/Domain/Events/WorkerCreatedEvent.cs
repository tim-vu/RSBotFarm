using Controller.Application.Domain.Entities;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class WorkerCreatedEvent : BaseDomainEvent
{
    public WorkerAccount WorkerAccount { get; }

    public WorkerCreatedEvent(WorkerAccount workerAccountAccount)
    {
        WorkerAccount = workerAccountAccount;
    }
}
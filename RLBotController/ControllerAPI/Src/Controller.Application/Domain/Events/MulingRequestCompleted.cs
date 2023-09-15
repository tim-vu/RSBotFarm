using Controller.Application.Domain.Entities;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class MulingRequestCompleted : BaseDomainEvent
{
    public MulingRequest MulingRequest { get;  }

    public MulingRequestCompleted(MulingRequest mulingRequest)
    {
        MulingRequest = mulingRequest;
    }
}
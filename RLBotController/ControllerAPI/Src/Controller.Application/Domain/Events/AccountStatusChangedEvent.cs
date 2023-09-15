using Controller.Application.Domain.Entities;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Events;

public class AccountStatusChangedEvent : BaseDomainEvent
{
    public Account Account { get;  }

    public AccountStatusChangedEvent(Account account)
    {
        Account = account;
    }
}
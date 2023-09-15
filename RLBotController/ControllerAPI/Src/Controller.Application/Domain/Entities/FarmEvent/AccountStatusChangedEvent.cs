using System;
using Controller.Application.Domain.Enums;

namespace Controller.Application.Domain.Entities.FarmEvent;

public class AccountStatusChangedEvent : FarmEvent
{
    public long AccountId { get; private set; }
    
    public string Username { get; private set; }
    
    public AccountStatus AccountStatus { get; private set; }

    
    // ReSharper disable once UnusedMember.Local
    private AccountStatusChangedEvent()
    {
        Username = null!;
    }
    
    public AccountStatusChangedEvent(Account account, DateTimeOffset at) : base(at)
    {
        AccountId = account.Id;
        Username = account.Username;
        AccountStatus = account.AccountStatus;
    }

    public override string GetMessage()
    {
        if (AccountStatus == AccountStatus.Banned)
        {
            return $"Account with username {Username} was banned";
        }

        return $"Account with username {Username} has an invalid password";
    }
}
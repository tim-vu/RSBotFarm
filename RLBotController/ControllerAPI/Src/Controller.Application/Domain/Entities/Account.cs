using System;
using Controller.Application.Domain.Enums;
using Controller.Application.Domain.Events;
using Controller.SharedKernel;

namespace Controller.Application.Domain.Entities;

public abstract class Account : Entity<long>
{
    public string Username { get; private set; }
    
    public string Password { get; private set; }
    
    public DateTimeOffset? MembershipExpiry { get; private set; }

    private AccountStatus _accountStatus;

    public void ResetPassword(string password)
    {
        _accountStatus = AccountStatus.Valid;
        Password = password;
    }
    
    public AccountStatus AccountStatus
    {
        get => _accountStatus;
        set
        {
            if (_accountStatus == AccountStatus.InvalidCredentials)
            {
                throw new InvalidOperationException("Cannot change the account status when the credentials are invalid");
            }
            
            AddEvent(new AccountStatusChangedEvent(this));
            _accountStatus = value;
        }
    }

    public void Assign(long scriptId, DateTimeOffset utcNow)
    {
        if (ScriptAssigment != null)
        {
            throw new InvalidOperationException("An allocation has already been made for this account");
        }

        ScriptAssigment = new ScriptAssigment(scriptId, utcNow);
    }
    
    public ScriptAssigment? ScriptAssigment { get; private set; }
    
    protected Account(string username, string password, DateTimeOffset? membershipExpiry)
    {
        Username = username;
        Password = password;
        MembershipExpiry = membershipExpiry;
    }
}
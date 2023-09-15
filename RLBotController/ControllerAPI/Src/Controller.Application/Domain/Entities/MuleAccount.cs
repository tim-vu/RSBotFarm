using System;

namespace Controller.Application.Domain.Entities;

public class MuleAccount : Account
{
    public MuleAccount(string username, string password, DateTimeOffset? membershipExpiry) : base(username, password, membershipExpiry)
    {
        
    }
}
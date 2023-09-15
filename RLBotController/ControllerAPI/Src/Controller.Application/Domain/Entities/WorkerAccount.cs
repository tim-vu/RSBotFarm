using System;

namespace Controller.Application.Domain.Entities;

public class WorkerAccount : Account
{
    
    public long? ScheduleId { get; set; }
    
    public WorkerAccount(string username, string password, DateTimeOffset? membershipExpiry) : base(username, password, membershipExpiry)
    {
    }
}
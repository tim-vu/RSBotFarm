using System;
using Controller.Application.Domain.Entities;
using Controller.Application.Domain.Events;

namespace Controller.Application.Domain.Factories.AccountFactory;

public class AccountFactory : IAccountFactory
{
    public MuleAccount CreateMuleAccount(string username, string password, DateTime? membershipExpiry)
    {
        var mule = new MuleAccount(username, password, membershipExpiry);
        mule.AddEvent(new MuleCreatedEvent(mule));
        return mule;
    }

    public WorkerAccount CreateWorkerAccount(string username, string password, DateTime? membershipExpiry)
    {
        var worker = new WorkerAccount(username, password, membershipExpiry);
        worker.AddEvent(new WorkerCreatedEvent(worker));
        return worker;
    }
}
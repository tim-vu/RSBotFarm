using System;
using Controller.Application.Domain.Entities;

namespace Controller.Application.Domain.Factories.AccountFactory;

public interface IAccountFactory
{
    MuleAccount CreateMuleAccount(string username, string password, DateTime? membershipExpiry);

    WorkerAccount CreateWorkerAccount(string username, string password, DateTime? membershipExpiry);
}
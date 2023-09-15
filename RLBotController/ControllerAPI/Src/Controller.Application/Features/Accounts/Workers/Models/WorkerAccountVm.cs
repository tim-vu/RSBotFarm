using Controller.Application.Domain.Enums;

namespace Controller.Application.Features.Accounts.Workers.Models;

public sealed record WorkerAccountVm(long Id, string Username, string Password, AccountStatus Status, long? MembershipExpiry);

namespace Controller.Application.Features.Events.Models;

public sealed record EventVm(long Id, long At, string Message);

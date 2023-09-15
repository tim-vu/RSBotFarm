using System;

namespace Controller.Application.Domain.Entities.FarmEvent;

public class MulingCompleted : FarmEvent
{
    public long MulingRequestId { get; private set; }
    
    public string Username { get; private set; }
    
    public int Amount { get; private set; }

    public MulingCompleted(long mulingRequestId, string username, int amount, DateTimeOffset at) : base(at)
    {
        MulingRequestId = mulingRequestId;
        Username = username;
        Amount = amount;
    }
    
    public override string GetMessage()
    {
        return $"{Username} transferred {FormatAmount(Amount)} gold";
    }

    private static string FormatAmount(int amount)
    {
        if (amount > 1_000_000)
        {
            return (amount / 1_000_000d).ToString("0.00") + "m";
        }

        return (amount / 1000d).ToString("0.00") + "k";
    }
}
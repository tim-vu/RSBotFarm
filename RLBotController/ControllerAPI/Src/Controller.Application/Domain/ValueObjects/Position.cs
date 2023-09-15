using System.Collections.Generic;
using Controller.SharedKernel;

namespace Controller.Application.Domain.ValueObjects;

public class Position : ValueObject
{
    public int X { get; private set; }
    
    public int Y { get; private set; }
    
    public int Z { get; private set; }

    public Position(int x, int y, int z)
    {
        X = x;
        Y = y;
        Z = z;
    }
    
    protected override IEnumerable<object> GetEqualityComponents()
    {
        yield return X;
        yield return Y;
        yield return Z;
    }

    public override object Clone()
    {
        return new Position(X, Y, Z);
    }
}
package net.rlbot.api.packet.util;

import java.math.BigInteger;

public class DMath {

    public static BigInteger modInverse(BigInteger val, int bits)
    {
        try
        {
            BigInteger shift = BigInteger.ONE.shiftLeft(bits);
            return val.modInverse(shift);
        }
        catch (ArithmeticException e)
        {
            return val;
        }
    }

    public static int modInverse(int val)
    {
        return modInverse(BigInteger.valueOf(val), 32).intValue();
    }

    public static long modInverse(long val)
    {
        return modInverse(BigInteger.valueOf(val), 64).longValue();
    }

}

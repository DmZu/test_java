package com.dmzu.server.classes;

/**
* Created by d.zhukov on 08.02.14.
*/
public enum EnumTcpCmd
{
    Version ((byte)1, (short)9),

    AutorizCli ((byte)2, (short)81),

    RegistrCli ((byte)3, (short)82),

    /// <summary>
    /// len 33
    /// X[8] Y[8] Z[8] AX[8] AY[8] AZ[8] VX[8] VY[8] VX[8]
    /// The position XY z_ A z_ VXY.
    /// </summary>
    PosXYZ_AXYZ_VXYZ ((byte)24, (short)73),

    LandInfo ((byte)60, (short)9),
    TimeNow ((byte)61, (short)9),
    LandData ((byte)63, (short)1025),
    LandCell ((byte)64, (short)7),

    CharMove ((byte)27, (short)9),
    CharLookTo ((byte)28, (short)33),

    unknown((byte)0, (short)0);


    private byte val;
    private short len;


    EnumTcpCmd(byte value, short length)
    {
        this.val = value;
        this.len = length;
    }

    public byte ToByte()
    {
        return val;
    }

    public short Length()
    {
        return len;
    }

    public static EnumTcpCmd GetCmdByByte(byte b)
    {
        for(EnumTcpCmd ob : EnumTcpCmd.values())
            if(ob.val == b)
                return ob;
        return unknown;
    }

}

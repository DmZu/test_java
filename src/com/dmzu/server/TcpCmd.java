package com.dmzu.server;

import java.nio.ByteBuffer;

/**
 * Created by d.zhukov on 27.01.14.
 */
public class TcpCmd {

    private byte id = 0;
    private ByteBuffer bbuf;

    public TcpCmd(byte ID_cmd, ByteBuffer DATA)
    {
        id = ID_cmd;

        bbuf = DATA;

    }

    public EnumTcpCmd GetCmd()
    {
        return EnumTcpCmd.GetCmdByByte(id);
    }

    public ByteBuffer GetData()
    {
        return bbuf;
    }
}

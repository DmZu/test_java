package com.company.global;

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

    public Cmd.TcpClient GetCmd()
    {
        return Cmd.TcpClient.GetCmdByByte(id);
    }

    public ByteBuffer GetData()
    {
        return bbuf;
    }
}

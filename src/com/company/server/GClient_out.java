package com.company.server;

import com.company.global.Cmd;
import com.company.global.Const;
import com.company.world.World;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by dm on 26.01.14.
 */
public class GClient_out extends Thread {

    private OutputStream out_s;

    public GClient_out(Socket cli_socket)
    {
        try
        {
            out_s = cli_socket.getOutputStream();
        }
        catch(Exception e)
            {System.out.println("Constructor error: "+e);} // вывод исключений

    }

    public void run()
    {
        try
        {

        }
        catch(Exception e)
        {System.out.println("init error: "+e);} // вывод исключений
    }


    public void SendVersion()
    {
        Send(ByteBuffer.allocate(9)
                .put(Cmd.TcpClient.Version.ToByte())
                .putDouble(Const.app_version)
                );
    }

    public void SendAutorizationResault(byte value)
    {
        Send(ByteBuffer.allocate(1)
                .put(Cmd.TcpClient.AutorizCli.ToByte())
                .put(value)
        );
    }

    private void Send(ByteBuffer buf)
    {

        ByteBuffer bbuf = ByteBuffer.allocate(buf.array().length + 2);

        bbuf.putShort(0, (short) buf.array().length);
        bbuf.put(buf);

        try
        {
            out_s.write(bbuf.array());
        }

        catch(Exception e)
        {System.out.println("Send error: "+e);}
    }

}

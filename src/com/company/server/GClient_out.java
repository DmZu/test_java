package com.company.server;

import com.company.global.Cmd;
import com.company.global.Const;
import com.company.world.World;
import com.company.world.objects.CharacterObject;
import com.company.world.objects.LandObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by dm on 26.01.14.
 */
public class GClient_out extends Thread {
    private Socket player_socket;

    private OutputStream out_s;
    private CharacterObject character;

    public GClient_out(Socket cli_socket)
    {
        player_socket = cli_socket;
        try
        {
            out_s = cli_socket.getOutputStream();
        }
        catch(Exception e)
        {System.out.println("Constructor error: "+e);} // вывод исключений

    }

    public void run()
    {
        while(player_socket.isConnected())
        {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SendCurPosLookVel();


        }

    }

    public void SetChar(CharacterObject char_ob)
    {
        character = char_ob;
        start();
    }

    public void SendCurPosLookVel()
    {
        Send(ByteBuffer.allocate(73)
                .put(Cmd.TcpClient.PosXYZ_AXYZ_VXYZ.ToByte())
                .putDouble(character.GetPos().x)
                .putDouble(character.GetPos().y)
                .putDouble(character.GetPos().z)
                .putDouble(character.GetLookVec().x)
                .putDouble(character.GetLookVec().y)
                .putDouble(character.GetLookVec().z)
                .putDouble(character.GetVelVec().x)
                .putDouble(character.GetVelVec().y)
                .putDouble(character.GetVelVec().z)
        );
    }

    public void SendLandDATA(short start_x, short start_y)
    {
        ByteBuffer buf = ByteBuffer.allocate(5 + Const.kvadrat_size * Const.kvadrat_size);
        buf.put(Cmd.TcpClient.LandData.ToByte());

        buf.putShort(start_x);
        buf.putShort(start_y);

        for(short ix=0; ix < Const.kvadrat_size; ix++)
            for(short iy=0; iy < Const.kvadrat_size; iy++)
            {
                LandObject cell = World.Inst().GetLandCell((short)(ix+start_x), (short)(iy+start_y));
                buf.put(cell.GetHeight());
                buf.put(cell.GetType().GetByteVal());

            }
        Send(buf);
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

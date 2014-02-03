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

    private int cur_kvad_x = -3200000, cur_kvad_y = -3200000;

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
        SendLandInfo();
        /*
        try {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        SendLandDATA();

        while(player_socket.isConnected())
        {


            try {
                Thread.currentThread().sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //SendCurPosLookVel();

            //
        }

    }

    public void SetChar(CharacterObject char_ob)
    {
        character = char_ob;
        character.SetConnect(this);
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

    public void SendLandInfo()
    {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.put(Cmd.TcpClient.LandInfo.ToByte());
        buf.put(World.Inst().GetDayTimeNow());
        buf.put((byte)Const.kvadrat_size);
        buf.put((byte)Const.meters_in_cell_xy);
        Send(buf);
    }

    private void SendLandDATA()
    {
        int x = (character.GetCellX()/Const.kvadrat_size);
        int y = (character.GetCellY()/Const.kvadrat_size);

        for(short ix=-1; ix < 2; ix++)
            for(short iy=-1; iy < 2; iy++)
                if(Math.abs(x + ix - cur_kvad_x)>=2 || Math.abs(y + iy - cur_kvad_y)>=2)
                    SendLandDATA(x+ix, y+iy);

        cur_kvad_x = x;
        cur_kvad_y = y;
    }

    private void SendLandDATA(int kvadrat_x, int kvadrat_y)
    {
        ByteBuffer buf = ByteBuffer.allocate(3 + (Const.kvadrat_size+1)*(Const.kvadrat_size+1) * 2);
        buf.put(Cmd.TcpClient.LandData.ToByte());

        buf.put((byte) kvadrat_x);
        buf.put((byte) kvadrat_y);

        for(short ix=0; ix < Const.kvadrat_size+1; ix++)
            for(short iy=0; iy < Const.kvadrat_size+1; iy++)
            {
                LandObject cell = World.Inst().GetLandCell(
                        (short)(ix+kvadrat_x*Const.kvadrat_size),
                        (short)(iy+kvadrat_y*Const.kvadrat_size)
                );
                buf.put(cell.GetHeight());
                buf.put(cell.GetType().GetByteVal());

            }
        Send(buf);
    }

    public void SendCell(LandObject cell)
    {
        Send(ByteBuffer.allocate(7)
                .put(Cmd.TcpClient.LandInfo.ToByte())
                .putShort(cell.GetCellX())
                .putShort(cell.GetCellY())
                .put(cell.GetHeight())
                .put(cell.GetType().GetByteVal())
        );
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
        Send(ByteBuffer.allocate(2)
                .put(Cmd.TcpClient.AutorizCli.ToByte())
                .put(value)
        );
    }

    public void SendRegistrationResault(byte value)
    {
        Send(ByteBuffer.allocate(2)
                .put(Cmd.TcpClient.RegistrCli.ToByte())
                .put(value)
        );
    }

    private void Send(ByteBuffer buf)
    {

        ByteBuffer bbuf = ByteBuffer.allocate(buf.array().length + 2);

        bbuf.putShort((short) buf.array().length);
        bbuf.put(buf.array());

        World.Inst().TextMessage("out cmd leng="+buf.array().length);
        //ByteBuffer.
        try
        {
            out_s.write(bbuf.array());
        }

        catch(Exception e)
        {System.out.println("Send error: "+e);}
    }

}

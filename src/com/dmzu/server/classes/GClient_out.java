package com.dmzu.server.classes;

import com.dmzu.Application;
import com.dmzu.world.AdapterToWorld;
import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.objects.LandObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by dm on 26.01.14.
 */
public class GClient_out extends Thread {
    private Socket player_socket;

    private OutputStream out_s;
    private int character_id;

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

        while(player_socket != null)
        {
            SendCurPosLookVel();
            SendTime();
            SendLandDATA();

            try {
                Thread.currentThread().sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void SetChar(int char_ob)
    {
        character_id = char_ob;
        //character_id.SetConnect(this);
        start();
    }

    private void SendCurPosLookVel()
    {
        Send(ByteBuffer.allocate(73)
                .put(EnumTcpCmd.PosXYZ_AXYZ_VXYZ.ToByte())
                .put(AdapterToWorld.GetObjPosByteBuff(character_id))
        );

    }

    private void SendTime()
    {
        //world.TextMessage("time=" + world.GetDayTimeNow());

        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.put(EnumTcpCmd.TimeNow.ToByte());
        buf.put(AdapterToWorld.GetDayTimeNow());
        Send(buf);

    }

    private void SendLandInfo()
    {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.put(EnumTcpCmd.LandInfo.ToByte());
        buf.put(AdapterToWorld.GetDayTimeNow());
        buf.put((byte)AdapterToWorld.GetKvadratSize());
        buf.put((byte)AdapterToWorld.GetMetersInCellXY());
        buf.put((byte)AdapterToWorld.GetMetersInCellZ());
        buf.putShort(AdapterToWorld.GetSize());
        Send(buf);
    }

    private void SendLandDATA()
    {
        int x = (AdapterToWorld.GetCellX(character_id)/AdapterToWorld.GetKvadratSize());
        int y = (AdapterToWorld.GetCellY(character_id)/AdapterToWorld.GetKvadratSize());

        for(short ix=-1; ix < 2; ix++)
            for(short iy=-1; iy < 2; iy++)
                if(Math.abs(x + ix - cur_kvad_x)>=2 || Math.abs(y + iy - cur_kvad_y)>=2)
                    SendLandDATA(x+ix, y+iy);

        cur_kvad_x = x;
        cur_kvad_y = y;
    }

    private void SendLandDATA(int kvadrat_x, int kvadrat_y)
    {
        ByteBuffer buf = ByteBuffer.allocate(3 + (AdapterToWorld.GetKvadratSize()+1)*(AdapterToWorld.GetKvadratSize()+1) * 2);
        buf.put(EnumTcpCmd.LandData.ToByte());

        buf.put((byte) kvadrat_x);
        buf.put((byte) kvadrat_y);

        for(short ix=0; ix < AdapterToWorld.GetKvadratSize()+1; ix++)
            for(short iy=0; iy < AdapterToWorld.GetKvadratSize()+1; iy++)
            {
                buf.put(AdapterToWorld.GetLandCellBytes(
                        (short) (ix + kvadrat_x * AdapterToWorld.GetKvadratSize()),
                        (short) (iy + kvadrat_y * AdapterToWorld.GetKvadratSize())
                ));
            }

        Send(buf);
    }

    public void SendCell(short x, short y)
    {
        Send(ByteBuffer.allocate(7)
                .put(EnumTcpCmd.LandCell.ToByte())
                .putShort(x)
                .putShort(y)
                .put(AdapterToWorld.GetLandCellBytes(x, y))
        );
    }

    public void SendVersion()
    {
        Send(ByteBuffer.allocate(9)
                .put(EnumTcpCmd.Version.ToByte())
                .putDouble(Application.version)
        );
    }



    public void SendAutorizationOk()
    {
        Send(ByteBuffer.allocate(2)
                .put(EnumTcpCmd.AutorizCli.ToByte())
                .put((byte)1)
        );
    }

    public void SendAutorizationEr()
    {
        Send(ByteBuffer.allocate(2)
                .put(EnumTcpCmd.AutorizCli.ToByte())
                .put((byte)0)
        );
    }

    public void SendRegistrationOk()
    {
        Send(ByteBuffer.allocate(2)
                .put(EnumTcpCmd.RegistrCli.ToByte())
                .put((byte)1)
        );
    }

    public void SendRegistrationEr()
    {
        Send(ByteBuffer.allocate(2)
                .put(EnumTcpCmd.RegistrCli.ToByte())
                .put((byte)0)
        );
    }

    private void Send(ByteBuffer buf)
    {

        ByteBuffer bbuf = ByteBuffer.allocate(buf.array().length + 2);

        bbuf.putShort((short) buf.array().length);
        bbuf.put(buf.array());

        AdapterToWorld.TextMessage("out cmd leng=" + buf.array().length);
        //ByteBuffer.
        try
        {
            out_s.write(bbuf.array());
        }

        catch(Exception e)
        {
            System.out.println("Send error: "+e);
            try {
                if(player_socket != null)
                    player_socket.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            player_socket = null;
        }

    }

}

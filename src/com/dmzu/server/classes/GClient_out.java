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
    private CharacterObject character;

    private int cur_kvad_x = -3200000, cur_kvad_y = -3200000;
    private AdapterToWorld world;
    public GClient_out(Socket cli_socket, AdapterToWorld world_)
    {
        world = world_;

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
        //SendLandDATA();

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


            //
        }

    }

    public void SetChar(CharacterObject char_ob)
    {
        character = char_ob;
        character.SetConnect(this);
        start();
    }

    private void SendCurPosLookVel()
    {
        Send(ByteBuffer.allocate(73)
                .put(EnumTcpCmd.PosXYZ_AXYZ_VXYZ.ToByte())
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

    private void SendTime()
    {
        //world.TextMessage("time=" + world.GetDayTimeNow());

        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.put(EnumTcpCmd.TimeNow.ToByte());
        buf.put(world.GetDayTimeNow());
        Send(buf);

    }

    private void SendLandInfo()
    {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.put(EnumTcpCmd.LandInfo.ToByte());
        buf.put(world.GetDayTimeNow());
        buf.put((byte)world.GetPropertes().get_Kvadrat_size());
        buf.put((byte)world.GetPropertes().get_Meters_in_cell_xy());
        Send(buf);
    }

    private void SendLandDATA()
    {
        int x = (character.GetCellX()/world.GetPropertes().get_Kvadrat_size());
        int y = (character.GetCellY()/world.GetPropertes().get_Kvadrat_size());

        //if(x>=0&&x<=world.GetPropertes().get_Size()/world.GetPropertes().get_Kvadrat_size()&&
                //y>=0&&y<=world.GetPropertes().get_Size()/world.GetPropertes().get_Kvadrat_size())
        {
        //world.GetLSize();

        for(short ix=-1; ix < 2; ix++)
            for(short iy=-1; iy < 2; iy++)
                if(Math.abs(x + ix - cur_kvad_x)>=2 || Math.abs(y + iy - cur_kvad_y)>=2)
                    SendLandDATA(x+ix, y+iy);
        }
        cur_kvad_x = x;
        cur_kvad_y = y;
    }

    private void SendLandDATA(int kvadrat_x, int kvadrat_y)
    {
        ByteBuffer buf = ByteBuffer.allocate(3 + (world.GetPropertes().get_Kvadrat_size()+1)*(world.GetPropertes().get_Kvadrat_size()+1) * 2);
        buf.put(EnumTcpCmd.LandData.ToByte());

        buf.put((byte) kvadrat_x);
        buf.put((byte) kvadrat_y);

        for(short ix=0; ix < world.GetPropertes().get_Kvadrat_size()+1; ix++)
            for(short iy=0; iy < world.GetPropertes().get_Kvadrat_size()+1; iy++)
            {
                LandObject cell = world.GetLandCell(
                        (short) (ix + kvadrat_x * world.GetPropertes().get_Kvadrat_size()),
                        (short) (iy + kvadrat_y * world.GetPropertes().get_Kvadrat_size())
                );
                if(cell!=null)
                {
                    buf.put(cell.GetHeightInByte());
                    buf.put(cell.GetType().GetByteVal());
                }
                else
                {buf.put((byte)0);buf.put((byte)0);}

            }
        Send(buf);
    }

    public void SendCell(LandObject cell)
    {
        Send(ByteBuffer.allocate(7)
                .put(EnumTcpCmd.LandCell.ToByte())
                .putShort(cell.GetCellX())
                .putShort(cell.GetCellY())
                .put(cell.GetHeightInByte())
                .put(cell.GetType().GetByteVal())
        );
    }

    public void SendVersion()
    {
        Send(ByteBuffer.allocate(9)
                .put(EnumTcpCmd.Version.ToByte())
                .putDouble(Application.version)
        );
    }



    public void SendAutorizationResault(byte value)
    {
        Send(ByteBuffer.allocate(2)
                .put(EnumTcpCmd.AutorizCli.ToByte())
                .put(value)
        );
    }

    public void SendRegistrationResault(byte value)
    {
        Send(ByteBuffer.allocate(2)
                .put(EnumTcpCmd.RegistrCli.ToByte())
                .put(value)
        );
    }

    private void Send(ByteBuffer buf)
    {

        ByteBuffer bbuf = ByteBuffer.allocate(buf.array().length + 2);

        bbuf.putShort((short) buf.array().length);
        bbuf.put(buf.array());

        world.TextMessage("out cmd leng=" + buf.array().length);
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

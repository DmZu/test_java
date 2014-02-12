package com.dmzu.server.classes;

import com.dmzu.Application;
import com.dmzu.world.AdapterClientToWorld;
import com.dmzu.world.classes.World;
import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.types.Vec3d;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by dm on 25.01.14.
 */
public class GClient_in extends Thread {
    private Socket player_socket;
    private InputStream in_s;

    private GClient_out client_out;

    private CharacterObject character;

    private AdapterClientToWorld world = World.Inst();

    public GClient_in(Socket p_socket) {
        player_socket = p_socket;
        // из сокета клиента берём поток входящих данных
        try {
            in_s = player_socket.getInputStream();
        } catch (Exception e) {
            System.out.println("Constructor error: " + e);
        } // вывод исключений

        try {
        //world.TextMessage("size="+player_socket.getReceiveBufferSize());
            player_socket.setSendBufferSize(1000000);
        }
        catch (Exception e) {
            System.out.println("set buffer size error: " + e);
        } // вывод исключений

        client_out = new GClient_out(p_socket, world);
        world.TextMessage("client connected");
        start();
    }

    public void run() {
        boolean is_login_ok = false;
        boolean is_version_ok = false;
        TcpCmd cmd;

        while (!is_login_ok && player_socket != null) {

            cmd = ReadNextCmd();

            world.TextMessage("CMD=" + cmd.GetCmd().ToByte());

            switch (cmd.GetCmd()) {
                case Version:

                    if (Application.version == cmd.GetData().getDouble(0)) {
                        world.TextMessage("version OK");
                        is_version_ok = true;
                    }

                    client_out.SendVersion();

                    break;

                case AutorizCli:
                    if (is_version_ok) {
                        String str = new String(cmd.GetData().array(), Charset.forName("UTF-32"));

                        CharacterObject ob = world.GetChar(str.split("\n")[0]);

                        if(ob != null)
                            if (ob.IsPass(str.split("\n")[1]))
                            {
                                is_login_ok = true;
                                client_out.SendAutorizationResault((byte)1);
                                character = ob;
                                client_out.SetChar(ob);
                            }

                        if(!is_login_ok)
                            client_out.SendAutorizationResault((byte)0);
                    }
                    break;

                case RegistrCli:
                    if (is_version_ok) {
                        String str = new String(cmd.GetData().array(), Charset.forName("UTF-32"));

                        CharacterObject ob = world.GetChar(str.split("\n")[0]);

                        if(ob == null)
                        {
                            new CharacterObject((byte)0, new Vec3d(0,0,0), str.split("\n")[0], str.split("\n")[1]);
                            client_out.SendRegistrationResault((byte)1);
                        }
                        else
                            client_out.SendRegistrationResault((byte)0);
                    }

                    break;

                default:
                    world.TextMessage("CMD=" + cmd.GetCmd().ToByte());
                    is_version_ok = false;

            }


        }


        while(player_socket != null)
        {
            cmd = ReadNextCmd();
            world.TextMessage("CMD=" + cmd.GetCmd().ToByte());

            switch (cmd.GetCmd())
            {
                case CharMove:
                    character.Move(cmd.GetData().getDouble());
                    break;

                case CharLookTo:
                    character.LookTo(new Vec3d(
                            cmd.GetData().getDouble(0),
                            cmd.GetData().getDouble(8),
                            cmd.GetData().getDouble(16)
                            ));
                    break;

                default:
                    world.TextMessage("CMD=" + cmd.GetCmd().ToByte());

            }


        }


    }

    private ByteBuffer Read(int i) {

        ByteBuffer out_buf = ByteBuffer.allocate(i);

        byte buf[] = new byte[i];

        int len = 0;

        while (len < i) {
            try {
                len += in_s.read(buf,0,i);

            } catch (Exception e) {
                System.out.println("Read error: " + e);
                try {
                    if(player_socket != null)
                        player_socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                player_socket = null;
            } // вывод исключений


        }



        out_buf = ByteBuffer.wrap(buf, 0, i);
        return out_buf;
    }


    private TcpCmd ReadNextCmd() {

/*
        ByteBuffer b = ByteBuffer.allocate(2);
        ByteBuffer b1 = ByteBuffer.allocate(2);
        b.putShort((short)9);
        b1 = Read(2);*/
        short length = Read(2).getShort();

        return new TcpCmd(Read(1).get(0), Read(length-1));

    }

}

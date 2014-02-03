package com.company.server;

import com.company.console.Console_UI;
import com.company.global.Const;
import com.company.global.TcpCmd;
import com.company.world.World;
import com.company.world.objects.CharacterObject;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.scene.layout.region.Margins;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


    public GClient_in(Socket p_socket) {
        player_socket = p_socket;

        // из сокета клиента берём поток входящих данных
        try {
            in_s = player_socket.getInputStream();
        } catch (Exception e) {
            System.out.println("Constructor error: " + e);
        } // вывод исключений



        client_out = new GClient_out(p_socket);
        World.Inst().TextMessage("client connected");
        start();
    }

    public void run() {
        boolean is_login_ok = false;
        boolean is_version_ok = false;
        TcpCmd cmd;

        while (!is_login_ok) {

            cmd = ReadNextCmd();

            World.Inst().TextMessage("CMD="+cmd.GetCmd().ToByte());

            switch (cmd.GetCmd()) {
                case Version:

                    if (Const.app_version == cmd.GetData().getDouble(0)) {
                        World.Inst().TextMessage("version OK");
                        is_version_ok = true;
                    }

                    client_out.SendVersion();

                    break;

                case AutorizCli:
                    if (is_version_ok) {
                        String str = new String(cmd.GetData().array(), Charset.forName("UTF-32"));

                        CharacterObject ob = World.Inst().GetChar(str.split("\n")[0]);

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


                    break;

                default:
                    World.Inst().TextMessage("CMD="+cmd.GetCmd().ToByte());
                    is_version_ok = false;

            }


        }


        while(player_socket.isConnected())
        {
            cmd = ReadNextCmd();

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
                    World.Inst().TextMessage("CMD="+cmd.GetCmd().ToByte());

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
                    player_socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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

package com.dmzu.server.classes;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 25.01.14.
 */
public class WorldServer extends Thread {


    private int TCPServerPort = 10011;
    private List<GClient> clients = new ArrayList<GClient>();

    public WorldServer()
    {
        start();
    }

    public void run()
    {
        try
        {
            int i = 0; // счётчик подключений


            ServerSocket server = new ServerSocket(TCPServerPort);

            System.out.println("server is started");

            // слушаем порт
            while(true)
            {
                // ждём нового подключения, после чего запускаем обработку клиента
                // в новый вычислительный поток и увеличиваем счётчик на единичку
                clients.add(new GClient(server.accept()));
                i++;
            }
        }
        catch(Exception e)
        {System.out.println("init error: "+e);} // вывод исключений

    }

    public GClient GetClient(int char_id)
    {
        for(GClient cli : clients)
            if(cli.GetCharID() == char_id && cli.GetClientOut().isAlive())
                return cli;

        return null;
    }

    public void UpdateObjPos(int id)
    {
        for(GClient cli : clients)
            if(cli.IsVisibleObj(id))
                cli.SetUpdateObj(id);

    }

    public void UpdateCell(short x, short y)
    {
        for(GClient cli : clients)
            cli.GetClientOut().SendCell(x, y);

    }

    public void DisconnectClient(GClient cli)
    {
        clients.remove(cli);
    }


}


package com.dmzu.server.classes;

import com.dmzu.world.classes.Const;

import java.net.ServerSocket;

/**
 * Created by dm on 25.01.14.
 */
public class WorldServer extends Thread {


    public WorldServer()
    {
        start();
    }

    public void run()
    {
        try
        {
            int i = 0; // счётчик подключений


            ServerSocket server = new ServerSocket(Const.TCPServerPort/*, 0,
                    InetAddress.getByName("localhost")*/);

            System.out.println("server is started");

            // слушаем порт
            while(true)
            {
                // ждём нового подключения, после чего запускаем обработку клиента
                // в новый вычислительный поток и увеличиваем счётчик на единичку
                new GClient_in(server.accept());
                i++;
            }
        }
        catch(Exception e)
        {System.out.println("init error: "+e);} // вывод исключений

    }




}


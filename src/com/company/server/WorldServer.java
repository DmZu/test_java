package com.company.server;

import java.net.InetAddress;
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

            // привинтить сокет на локалхост, порт 3128
            ServerSocket server = new ServerSocket(3128, 0,
                    InetAddress.getByName("localhost"));

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


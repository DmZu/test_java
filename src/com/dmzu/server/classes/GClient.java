package com.dmzu.server.classes;

import java.net.Socket;

/**
 * Created by dm on 14.02.14.
 */
public class GClient {

    private GClient_in client_in;
    private GClient_out client_out;

    private int char_id=-1;

    public GClient(Socket p_socket)
    {
        client_in = new GClient_in(p_socket);
        client_out = new GClient_out(p_socket);
    }

}

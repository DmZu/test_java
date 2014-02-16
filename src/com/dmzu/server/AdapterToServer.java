package com.dmzu.server;

import com.dmzu.server.classes.WorldServer;

/**
 * Created by dm on 14.02.14.
 */
public class AdapterToServer {

    private static WorldServer server;

    public static void Start()
    {
        server = new WorldServer();
    }

    public static void UpdateCell(short x, short y)
    {
        if(server!=null)
            server.UpdateCell(x, y);
    }

    public static void UpdateObjPos(int id)
    {
        if(server!=null)
            server.UpdateObjPos(id);
    }

    public static void SetVisibleObjs(int char_id, int[] objs)
    {
        if(server!=null)
            if(server.GetClient(char_id) != null)
                server.GetClient(char_id).SetVisibleObjs(objs);
    }

    public static void DisconnectClient(int char_id)
    {
        server.DisconnectClient(server.GetClient(char_id));
    }

}

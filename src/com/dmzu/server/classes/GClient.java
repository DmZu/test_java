package com.dmzu.server.classes;

import com.dmzu.world.AdapterToWorld;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 14.02.14.
 */
public class GClient {

    private GClient_in client_in;
    private GClient_out client_out;

    private int char_id=-1;

    private int[] visible_objs;
    private List<Integer> need_update_objs = new ArrayList<Integer>();

    public GClient(Socket p_socket)
    {
        visible_objs = new int[0];

        client_in = new GClient_in(this, p_socket);
        client_out = new GClient_out(this, p_socket);
        client_in.start();;
    }

    public int GetCharID()
    {
        return char_id;
    }

    protected void SetCharID(int id)
    {
        char_id = id;
    }

    protected GClient_out GetClientOut()
    {
        return client_out;
    }

    public  void SetVisibleObjs(int[] objs)
    {
        for(int obj : objs)
        {
            boolean exist = false;

            for(int obj1 : visible_objs)
                if(obj == obj1)
                    exist = true;

            if(!exist)
                SetUpdateObj(obj);
        }

        visible_objs = objs;

        GetClientOut().SendVisibleObjs(visible_objs);
    }

    public  boolean IsVisibleObj(int obj_id)
    {
        for(int obj : visible_objs)
            if(obj == obj_id)
                return true;



        return false;
    }

    public synchronized void SetUpdateObj(int id)
    {

        //synchronized (need_update_objs)
        {
            boolean exist = false;
            for(int i : need_update_objs)
                if (i==id)
                    exist = true;

            if(!exist)
                need_update_objs.add(id);
        }
    }

    public synchronized List<Integer> GetUpdateList()
    {
        List<Integer> list = new ArrayList<Integer>();

        for(Integer i : need_update_objs)
            list.add(i);

        need_update_objs.clear();
        return list;
    }
/*
    public void ClearUpdateList()
    {
        need_update_objs.clear();
    }*/
}

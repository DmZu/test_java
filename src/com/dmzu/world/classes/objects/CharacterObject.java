package com.dmzu.world.classes.objects;

import com.dmzu.server.classes.GClient_out;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.zhukov on 30.01.14.
 */
public class CharacterObject extends LifeObject implements AI_in_Interface {

    private String char_name = "def", char_pass = "def";

    private double pain = 0;
    private double hunger = 0;
    private double thirst = 0;
    private List<BaseObject> visible_objs = new ArrayList<BaseObject>();

    private BaseObject target;

    private double time_to_message = 0.3d;

    private GClient_out tcp_client;

    public CharacterObject(byte type, Vec3d pos, String name, String pass)
    {
        super(pos);
        char_name = name;
        char_pass = pass;
    }

    @Override
    protected void Work(double time_sec)
    {
        LifeCalc(time_sec);

        time_to_message-=time_sec;
        if(time_to_message<=0)
        {
            //World.Inst().TextMessage("POS="+GetPos());
            time_to_message = 0.3d;
        }
    }

    public boolean IsConnect()
    {
        if(tcp_client == null)
            return false;
        else
            return true;
    }

    public GClient_out GetTcpClient()
    {
        return tcp_client;
    }

    public void SetConnect(GClient_out client)
    {
        tcp_client = client;
    }

    public String GetName()
    {
        return char_name;
    }

    public boolean IsPass(String pass)
    {
        if(pass.equals(char_pass))
            return true;
        else
            return false;
    }


    public void SetVisibleObjects(List<BaseObject> obj_list)
    {
        visible_objs = obj_list;
    }

    public void SetHunger(double value)
    {
        hunger = value;
    }


    public void SetThirst(double value)
    {
        thirst = value;
    }


    public void SetPain(double value)
    {
        pain = value;
    }
}

package com.dmzu.world.classes.objects;

import com.dmzu.server.AdapterToServer;
import com.dmzu.world.AdapterToWorld;
import com.dmzu.world.classes.World;
import com.dmzu.world.classes.objects.abstr.BaseObject;
import com.dmzu.world.classes.objects.abstr.LifeObject;
import com.dmzu.world.classes.objects.abstr.StaticObject;
import com.dmzu.world.classes.types.Enums;
import com.dmzu.world.classes.types.Vec3d;

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
    private double time_to_update_vis = 5d;

    public CharacterObject(byte type, Vec3d pos, String name, String pass)
    {
        super(pos);
        char_name = name;
        char_pass = pass;

        SetType(Enums.ObjTps.Character);
    }

    @Override
    protected void Work(double time_sec)
    {
        LifeCalc(time_sec);

        time_to_update_vis-=time_sec;
        if(time_to_update_vis<=0)
        {
            UpdateVizibleToServ();
            time_to_update_vis = 5d;
        }
    }


    public boolean IsConnect()
    {
        /*
        if(tcp_client == null)
            return false;
        else*/
            return true;
    }
/*
    public GClient_out GetTcpClient()
    {
        return tcp_client;
    }

    public void SetConnect(GClient_out client)
    {
        tcp_client = client;
    }
*/
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

    public void SetJob(byte job)
    {
        switch (job)
        {
            case 1:
                World.Inst().GetLandCell(GetCellX(),GetCellY()).GetCell();
                break;

            case 2:
                World.Inst().GetLandCell(GetCellX(),GetCellY()).PutCell();
                break;

            case 3:
                ConstructionObject ob = new ConstructionObject(GetPos(),GetLookVec());
                World.Inst().AddConstruction(ob);
                break;
        }
    }

    private void UpdateVizibleToServ()
    {
        int count_stat=0;
        for(BaseObject bob : visible_objs)
            if(bob instanceof StaticObject)
                count_stat++;

        int[] ids = new int[count_stat];

        count_stat=0;

        boolean is_char_vis = false;

        for(BaseObject bob : visible_objs)
            if(bob instanceof StaticObject)
            {
                if(((StaticObject)bob).GetID() == GetID())
                    is_char_vis = true;
                ids[count_stat] = ((StaticObject)bob).GetID();
                count_stat++;
            }

        if(!is_char_vis)
            World.Inst().TextMessage("no vis");


        AdapterToServer.SetVisibleObjs(GetID(), ids);
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

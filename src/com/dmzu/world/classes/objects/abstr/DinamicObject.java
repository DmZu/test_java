package com.dmzu.world.classes.objects.abstr;

import com.dmzu.world.classes.World;
import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.types.Vec3d;

/**
 * Created by Людмила on 07.01.14.
 */
public abstract class DinamicObject extends StaticObject {

    private Vec3d vel;

    private int work_dist = 0;

    private double front_force = 0;

    public DinamicObject(Vec3d _vel, Vec3d pos, Vec3d ang)
    {
        super(pos, ang);
        vel = new Vec3d(_vel);
    }


    protected void DinamicCalc(double time_sec)
    {
        double h = World.Inst().GetDownHeightFromPoint(GetPos());

        Vec3d d = new Vec3d(GetLookVec());
        //Vec3d d = GetLookVector();



        d.mul(front_force / (GetMass()/10.0));

        if(this instanceof CharacterObject)
        {
            //World.Inst().TextMessage("VEL="+d + "  F="+front_force);
        }
        vel=new Vec3d(d.x,d.y,vel.z);

        if(GetPos().z > h)
        {
            vel.z -= time_sec*10;
        }
        else
            vel.z = 0;

        Vec3d v = new Vec3d(vel);

        v.mul(time_sec);
        MovePos(v);
    }

    protected void AddForceFront(Double value)
    {
        front_force = value;
        if(this instanceof CharacterObject)
        {
            //World.Inst().TextMessage("VEL="+d);
            front_force*=10;
        }
/*
        Vec3d d = new Vec3d(GetLookVec());
        //Vec3d d = GetLookVector();

        if(this instanceof CharacterObject)
        {
            //World.Inst().TextMessage("VEL="+d);
            front_force*=10;
        }

        d.mul(front_force / (GetMass()/10.0));

        if(this instanceof CharacterObject)
        {
            //World.Inst().TextMessage("VEL="+d + "  F="+front_force);
        }
        vel.set(d);*/
    }


    protected int GetWorkAndClear()
    {
        int _work = work_dist * GetMass()/10;
        work_dist = 0;
        return _work;
    }

    public void AddWork(int w)
    {
        work_dist += w;
    }

    public Vec3d GetVelVec()
    {
        return vel;
    }

}

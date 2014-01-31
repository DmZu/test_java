package com.company.world.objects;

import com.company.world.World;
import com.sun.javafx.geom.Vec3d;

/**
 * Created by Людмила on 07.01.14.
 */
public abstract class DinamicObject extends StaticObject {

    private Vec3d vel;

    private int work_dist = 0;


    public DinamicObject(Vec3d _vel, Vec3d pos, Vec3d ang)
    {
        super(pos, ang);
        vel = new Vec3d(_vel);
    }


    protected void DinamicCalc(double time_sec)
    {
        double h = World.Inst().GetLandPointHeight(GetPos().x, GetPos().y);
        if(GetPos().z > h)
        {
            vel.z -= time_sec;
        }
        else
            vel.z = 0;

        Vec3d v = new Vec3d(vel);

        v.mul(time_sec);
        MovePos(v);
    }

    protected void AddForceFront(Double value)
    {
        Vec3d d = GetLookVector();

        d.mul(value / (GetMass()/10.0));

        vel.set(d);
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

}

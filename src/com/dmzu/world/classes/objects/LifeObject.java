package com.dmzu.world.classes.objects;

import com.dmzu.world.classes.World;
import com.dmzu.type.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Людмила on 08.01.14.
 */
public abstract class LifeObject extends DinamicObject implements AI_out_Interface {

    private final static double recalc_bio_time = 2;
    private double bio_time = Double.NaN;

    LifePropertes atr;

    private boolean is_alive = true;
    private double pain = 0;
    private double hunger = 0;
    private double thirst = 0;
    private List<BaseObject> visible_objs = new ArrayList<BaseObject>();


    private double job_time_sec = 0;

    public LifeObject(Vec3d pos)
    {
        super(new Vec3d(0,0,0),pos,new Vec3d(0,1,0));
        atr = LifePropertes.NewDefAnim(this);
    }



    protected boolean LifeCalc(double time_sec)
    {
        DinamicCalc(time_sec);

        if (Double.isNaN(bio_time))
            bio_time = 0;
        else
            bio_time += time_sec;



        if (bio_time > recalc_bio_time)
        {
            int all_work = GetWorkAndClear();
            all_work += (int)(bio_time * (GetMass() + GetMatCountByType(Enums.GMaterials.Meat)));


            if(!(this instanceof CharacterObject))
            if (RemoveMaterial(Enums.GMaterials.Fat, all_work / 1000) != 0)
            {
                int i = GetMatCountByType(Enums.GMaterials.Bone) / 10;

                if(RemoveMaterial(Enums.GMaterials.Meat, i) == 0)
                    AddMaterial(Enums.GMaterials.Fat, i);
            }



            RemoveMaterial(Enums.GMaterials.Woter, all_work / 100);


            int woter = GetMatCountByType(Enums.GMaterials.Woter);
            int meat = GetMatCountByType(Enums.GMaterials.Meat);
            int fat = GetMatCountByType(Enums.GMaterials.Fat);

            if (woter == 0 ||
                    meat == 0)
            {
                //Console.WriteLine("death");
                if(!(this instanceof CharacterObject))
                    Destroy();

                is_alive = false;

            }


            visible_objs = GetVisibleObjs();

            if (woter > GetMatCountByType(Enums.GMaterials.Bone) / 2)
                thirst = 0;
            else
                thirst = 1;

            if (fat > GetMatCountByType(Enums.GMaterials.Bone) / 8)
                hunger = 0;
            else
                hunger = 1;




            bio_time = 0;
        }

        SetParamsToChild();


        if (job_time_sec > 0)
        {
            job_time_sec -= time_sec;

            if (job_time_sec < 0) job_time_sec = 0;

            return false;
        }
        else
            return true;
    }
    public void DebugOut()
    {
        World.Inst().TextMessage("Woter: " + GetMatCountByType(Enums.GMaterials.Woter) + " Meat: " + GetMatCountByType(Enums.GMaterials.Meat) + " Mass: " + GetMass());
        World.Inst().TextMessage("Pos: " + GetPos());
    }


    private void SetParamsToChild()
    {
        AI_in_Interface child = (AI_in_Interface)this;

        child.SetVisibleObjects(visible_objs);
        child.SetPain(pain);
        child.SetThirst(thirst);
        child.SetHunger(hunger);

    }

    private List<BaseObject> GetVisibleObjs()
    {
        List<BaseObject> list = new ArrayList<BaseObject>();

        short vision = GetVisionDist();

        for (int ix = -vision; ix < vision; ix++)
            for (int iy = -vision; iy < vision; iy++)
            {
                short x = (short)(GetCellX() + ix);
                short y = (short)(GetCellY() + iy);

                list.add(World.Inst().GetLandCell(x, y));
            }

        return list;
    }

    private short GetVisionDist()
    {
        if(World.Inst().GetDayTimeNow() < 0)
            return atr.night_vision;
        else
            return atr.day_vision;
    }


    public boolean IsAlive()
    {
        return is_alive;
    }



    /// From AI Events

    public void LookTo(BaseObject target)
    {
        Vec3d val = new Vec3d(1,0,0);

        if (target instanceof LandObject)
        {
            val.set(((LandObject) target).GetPos());

            val.sub(GetPos());

            val.normalize();


        }

        LookTo(val);
    }

    public void LookTo(Vec3d value)
    {
        SetLookVector(value);
    }
    public void Move(Double value)
    {
        AddForceFront(GetMatCountByType(Enums.GMaterials.Meat) * value);
    }

    public void Attack(Vec3d value)
    {

    }

    public void Eat(BaseObject target)
    {
        if ( job_time_sec == 0 )
        {

            int woter = target.GetMatCountByType(Enums.GMaterials.Woter);

            if (woter > 0)
            {
                target.RemoveMaterial(Enums.GMaterials.Woter, woter);

                if (woter + GetMatCountByType(Enums.GMaterials.Woter) > GetMatCountByType(Enums.GMaterials.Bone) * 1.5)
                    woter = (int)((GetMatCountByType(Enums.GMaterials.Bone) * 1.5) - GetMatCountByType(Enums.GMaterials.Woter));

                this.AddMaterial( Enums.GMaterials.Woter,woter );


            }

            int food = target.GetMatCountByType(atr.mat_for_eat.get(0));

            if (food > 0)
            {
                target.RemoveMaterial(atr.mat_for_eat.get(0), food);

                if (food + GetMatCountByType(Enums.GMaterials.Fat) > GetMatCountByType(Enums.GMaterials.Bone) )
                    food = (int)((GetMatCountByType(Enums.GMaterials.Bone) * 1.5) - GetMatCountByType(Enums.GMaterials.Fat));
                this.AddMaterial(Enums.GMaterials.Fat, food);

            }


            job_time_sec += 2;
        }
    }

}

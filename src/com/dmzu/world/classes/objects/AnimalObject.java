package com.dmzu.world.classes.objects;

import com.dmzu.type.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Людмила on 08.01.14.
 */
public class AnimalObject extends LifeObject implements AI_in_Interface {

    private double pain = 0;
    private double hunger = 0;
    private double thirst = 0;
    private List<BaseObject> visible_objs = new ArrayList<BaseObject>();

    private BaseObject target;

    public AnimalObject(byte type, Vec3d pos)
    {
        super(pos);
    }


    @Override
    protected void Work(double time_sec)
    {
        if (IsAlive())
        {
            LifeCalc(time_sec);


            if (thirst > 0)
            {
                if(target==null)

                    target = GetNearestObjectByMat(Enums.GMaterials.Woter);

                if (target instanceof LandObject)
                {
                    LandObject o = (LandObject)target;

                    Vec3d delta = new Vec3d(o.GetPos());
                    delta.sub(GetPos());

                    double dist = delta.length();
                    Vec3d pos = new Vec3d(o.GetPos());


                    if (dist < 2)
                    {
                        Move(0d);
                        Eat(target);
                        thirst = 0;
                        target = null;
                    }
                    else
                    {
                        LookTo(target);

                        Move(1d);
                    }
                }
            }
            else
                Move(0d);
        }
    }



    private BaseObject GetNearestObjectByMat( Enums.GMaterials material )
    {
        if (visible_objs.size() == 0)
            return null;

        BaseObject obj = null;


        for (BaseObject element : visible_objs)
        {
            if (element != null && element.GetMatCountByType(material) > 0)
            {
                if (obj == null) obj = element;


                if (Math.abs(obj.GetCellX() - this.GetCellX()) + Math.abs(obj.GetCellY() - this.GetCellY())
                        > Math.abs(element.GetCellX() - this.GetCellX()) + Math.abs(element.GetCellY() - this.GetCellY()))
                    obj = element;
            }
        }


        return obj;
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

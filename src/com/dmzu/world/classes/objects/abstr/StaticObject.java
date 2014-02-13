package com.dmzu.world.classes.objects.abstr;

import com.dmzu.world.classes.World;
import com.dmzu.world.classes.types.ObjectID;
import com.dmzu.world.classes.types.Vec3d;

/**
 * Created by Людмила on 07.01.14.
 */
public abstract class StaticObject extends BaseObject {

    private Vec3d pos;
    private Vec3d look_vector;

    private int id;

    public StaticObject(Vec3d _pos, Vec3d _look_vector)
    {
        super((short)(_pos.x / World.Inst().GetPropertes().get_Meters_in_cell_xy()), (short)(_pos.y / World.Inst().GetPropertes().get_Meters_in_cell_xy()));
        pos = new Vec3d(_pos);
        look_vector = new Vec3d(_look_vector);

        id = ObjectID.CreateID(this);
    }

    protected void SetLookVector(Vec3d value)
    {
        look_vector = value;
    }

    protected void MovePos(Vec3d value)
    {
        pos.add(value);

        if(pos.x<0) pos.x=0;
        if(pos.y<0) pos.y=0;
        if(pos.x>=(World.Inst().GetPropertes().get_Size()-1)*World.Inst().GetPropertes().get_Meters_in_cell_xy())
            pos.x=(World.Inst().GetPropertes().get_Size()-1)*World.Inst().GetPropertes().get_Meters_in_cell_xy();

        if(pos.y>=(World.Inst().GetPropertes().get_Size()-1)*World.Inst().GetPropertes().get_Meters_in_cell_xy())
            pos.y=(World.Inst().GetPropertes().get_Size()-1)*World.Inst().GetPropertes().get_Meters_in_cell_xy();

        SetCell();

        double h = World.Inst().GetDownHeightFromPoint(GetPos());
        if(pos.z < h)
            pos.z = h;

    }

    public int GetID()
    {
        return id;
    }

    public Vec3d GetPos()
    {
        return pos;
    }

    public Vec3d GetLookVec()
    {
        return look_vector;
    }

}

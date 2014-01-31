package com.company.world.objects;

import com.company.global.Const;
import com.company.world.World;
import com.sun.javafx.geom.Vec3d;

/**
 * Created by Людмила on 07.01.14.
 */
public abstract class StaticObject extends BaseObject {

    private Vec3d pos;
    private Vec3d look_vector;

    public StaticObject(Vec3d _pos, Vec3d _look_vector)
    {
        super((short)(_pos.x / Const.meters_in_cell_xy), (short)(_pos.y / Const.meters_in_cell_xy));
        pos = new Vec3d(_pos);
        look_vector = new Vec3d(_look_vector);
    }

    protected void SetLookVector(Vec3d value)
    {
        look_vector = value;
    }
    protected Vec3d GetLookVector()
    {
        return look_vector;
    }

    protected void MovePos(Vec3d value)
    {
        pos.add(value);

        SetCell();

        double h = World.Inst().GetLandPointHeight(GetPos().x, GetPos().y);
        if(pos.z < h)
            pos.z = h;

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

package com.dmzu.world.classes.objects;

import com.dmzu.world.classes.objects.abstr.StaticObject;
import com.dmzu.world.classes.types.Enums;
import com.dmzu.world.classes.types.Vec3d;

/**
 * Created by dm on 16.02.14.
 */
public class ConstructionObject extends StaticObject {

    public ConstructionObject(Vec3d pos, Vec3d ang)
    {
        super(pos, ang);

        SetType(Enums.ObjTps.Default);

    }

    @Override
    protected void Work(double time_sec)
    {

    }
}

package com.dmzu.world.classes.objects;

import com.sun.javafx.geom.Vec3d;

/**
 * Created by Людмила on 08.01.14.
 */
public interface AI_out_Interface {
    /// From AI


    void LookTo(BaseObject target);

    void LookTo(Vec3d value);

    void Move(Double value);

    void Attack(Vec3d value);

    void Eat(BaseObject target);


}
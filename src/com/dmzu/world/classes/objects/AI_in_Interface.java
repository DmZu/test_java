package com.dmzu.world.classes.objects;

import java.util.List;

/**
 * Created by Людмила on 08.01.14.
 */
public interface AI_in_Interface {

    void SetVisibleObjects(List<BaseObject> obj_list);

    void SetHunger(double value);

    void SetThirst(double value);

    void SetPain(double value);

}

package com.dmzu.world.classes.types;

import com.dmzu.world.classes.objects.abstr.StaticObject;

/**
 * Created by d.zhukov on 13.02.14.
 */
public class ObjectID {

    private static StaticObject[] All = new StaticObject[1000000];

    public static StaticObject GetObject(int id)
    {
        return All[id];
    }

    public static int CreateID(StaticObject object_)
    {

        for(int i = 0; i < All.length; i++)
            if(All[i] == null)
            {
                All[i] = object_;
                return i;
            }

        return -1;
    }

    public static void RemoveID(int id)
    {
        All[id] = null;
    }
}

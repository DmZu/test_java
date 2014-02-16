package com.dmzu.world.classes;

import com.dmzu.world.classes.objects.abstr.BaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Людмила on 06.01.14.
 */
public class ObjectsThread extends Thread {
    private List<BaseObject> obj_list = new ArrayList<BaseObject>();

    //private Type cls_type = BaseObject.class.getGenericSuperclass();

    public void Add(BaseObject obj)
    {
        obj_list.add(obj);
    }

    public ObjectsThread(BaseObject[][] array)
    {
        for (int ix = 0; ix < array.length; ix++)
            for (int iy = 0; iy < array[ix].length; iy++)
                obj_list.add(array[ix][iy]);

        //this.start();

    }
    public ObjectsThread()
    {
        //this.start();
    }

    public double GetTickTime()
    {
        if(obj_list.size() == 0)
            return 0;

        return obj_list.get(0).GetTickTimeSec();
    }

    @Override
    public synchronized void run() {
        World.Inst().TextMessage("ObjectsThread Was Started. List size: " + obj_list.size());

        List<BaseObject> Remove_List = new ArrayList<BaseObject>();

        while (true)
        {

            if(GetTickTime() <= 0.001)
                try {
                    Thread.currentThread().sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            for(BaseObject obj : obj_list)
            {

                if(obj.IsDestroy())
                    Remove_List.add(obj);
                else
                    obj.OneTick();

            }

            while(Remove_List.size() > 0)
            {
                //World.Inst().TextMessage("Rem_list" + Remove_List.size());
                obj_list.remove(Remove_List.get(0));
                Remove_List.remove(0);
            }

        }
    }


    public BaseObject Get(int i)
    {
        if(obj_list!= null && i<obj_list.size())
            return obj_list.get(i);
        else
            return null;
    }
}

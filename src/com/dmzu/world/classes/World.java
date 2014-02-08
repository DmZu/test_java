package com.dmzu.world.classes;


import com.dmzu.server.WorldServer;
import com.dmzu.world.IAdminToWorld;
import com.dmzu.world.IToAdmin;
import com.dmzu.world.classes.objects.AnimalObject;
import com.dmzu.world.classes.objects.BaseObject;
import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.objects.LandObject;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;

/**
 * Created by Людмила on 06.01.14.
 */
public class World extends Thread implements IAdminToWorld {

    private List<IToAdmin> out_ui = new ArrayList<IToAdmin>();

    private ObjectsThread Land_objects, Animal_objects, Char_objects;
    private LandObject[][] Land_Matrix;
    private byte day_time_now;

    private static World inst = new World();

    public static World Inst()    {    return inst;    }

    private World(){}

    @Override
    public synchronized void run() {


        while (true)
        {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            day_time_now++;


            //TextMessage("time="+day_time_now);
            GetTickTime();
        }
    }





    ///Admins interface methods

    public void AddUI(IToAdmin ui)
    {
        out_ui.add(ui);
    }

    public void Create(String name, short size)
    {

        Land_Matrix = LandMethods.GenerateLandMatrix(size);
        LandMethods.GenerateAnimals(size, size * 5);

        //for(int i = 0; i < 10000; i++)
        new CharacterObject(
                (byte)0,
                new Vec3d(
                        size * Const.meters_in_cell_xy / 2,
                        size * Const.meters_in_cell_xy / 2,
                        256d),
                "def",
                "111");

        day_time_now = 0;

        TextMessage("Created Land. Name: " + name + " Size: " + size);

    }

    public void Start(String name)
    {


        Land_objects = new ObjectsThread(Land_Matrix);

        Animal_objects = new ObjectsThread();
        Char_objects = new ObjectsThread();

        for (int ix = 0; ix < Land_Matrix.length; ix++)
            for (int iy = 0; iy < Land_Matrix[ix].length; iy++)
                for (BaseObject elem : Land_Matrix[ix][iy].GetConteiner())
                {
                    if(elem instanceof AnimalObject)
                        Animal_objects.Add(elem);
                    if(elem instanceof CharacterObject)
                        Char_objects.Add(elem);
                }


        Land_objects.start();
        Animal_objects.start();
        Char_objects.start();

        TextMessage("Started Land. Name: " + name + " Size: " + Land_Matrix.length);


        this.start();
        new WorldServer();

    }

    public void Stop()
    {}

    public String GetTickTime()
    {
        String str = "Unknown";
        if(Animal_objects == null)
            return "world is not runing";

        AnimalObject ob = (AnimalObject)Animal_objects.Get(5100);

        if(ob==null)
            ob = (AnimalObject)Animal_objects.Get(0);

        if(ob!=null)
        {
            str = "Anim="+ob.GetTickTimeSec();
            //ob.DebugOut();
        }

        return str;
    }





    ///Client Interface methods

    public int GetLSize()
    {
        if(Land_Matrix!= null)
            return Land_Matrix.length;
        else
            return 0;
    }

    public LandObject GetLandCell(short ix, short iy)
    {
        if (ix < 0 || iy < 0 || ix >= Land_Matrix.length || iy >= Land_Matrix[ix].length)
            return null;

        return Land_Matrix[ix][iy];
    }

    public double GetDownHeightFromPoint(Vec3d point)
    {
        //TODO: poluchat visotu s terraina. uchitivaia visotu sosednih kletok.

        double h = (double)GetLandCell((short)(point.x / Const.meters_in_cell_xy),(short)(point.y / Const.meters_in_cell_xy)).GetHeight();


        return h;
    }

    public byte GetDayTimeNow()
    {
        return day_time_now;
    }

    public CharacterObject GetChar(String name)
    {
        int i = 0;
        CharacterObject ob = (CharacterObject)Char_objects.Get(i);

        while(ob != null)
        {
            ob = (CharacterObject)Char_objects.Get(i);
            if(ob == null || ob.GetName().equals(name))
                break;
            i++;
        }

        return ob;
    }
    public void TextMessage(String message)
    {
        for(IToAdmin ob : out_ui)
            ob.TextMessage(message);
    }
}

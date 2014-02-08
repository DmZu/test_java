package com.dmzu.world.classes;

import com.dmzu.world.classes.objects.Enums;
import com.dmzu.world.classes.objects.AnimalObject;
import com.dmzu.world.classes.objects.BaseObject;
import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.objects.LandObject;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Людмила on 06.01.14.
 */
public class LandMethods {


    private static byte[][] GenerateHeightsMap(short size)
    {


        byte[][] heights_map = new byte[size][size];

        int hei_ = Const.see_level + 5;
        ///plato
        for (int ix = 0; ix < size; ix++)
            for (int iy = 0; iy < size; iy++)
            {

                double vis = ((ix - (size / 2)) * (ix - (size / 2))) +
                        ((iy - (size / 2)) * (iy - (size / 2)));

                vis = ((size / 2) - Math.sqrt(vis));
                if (vis >= hei_) vis = hei_;
                if (vis <= 0) vis = 0;
                vis = Math.pow((vis / hei_), 2 - ((vis / hei_) * 1.9)) * hei_;

                heights_map[ix][iy] = (byte)vis;
            }

///Create holm
        heights_map = AddToHeiMap(heights_map, CreateObriv(100, 5), (heights_map.length * heights_map.length) / 10000);

        heights_map = AddToHeiMap(heights_map, CreateHolm(100, 20), (heights_map.length * heights_map.length) / 3000);

        heights_map = AddToHeiMap(heights_map, CreateHolm(300, 60), (heights_map.length * heights_map.length) / 500);

        return heights_map;
    }

    private static byte[][] CreateHolm(int size_h, int hei_)
    {
        byte[][] holm = new byte[size_h][size_h];
        for (short ix = 0; ix < size_h; ix++)
            for (short iy = 0; iy < size_h; iy++)
            {

                double vis = ((ix - (size_h / 2)) * (ix - (size_h / 2))) +
                        ((iy - (size_h / 2)) * (iy - (size_h / 2)));

                vis = ((size_h / 2) - Math.sqrt(vis));
                if (vis >= hei_) vis = hei_;
                if (vis <= 0) vis = 0;

                vis = Math.pow((vis / hei_), 2 - ((vis / hei_) * 1.9)) * hei_;


                holm[ix][iy] = (byte)vis;

                //Log.WriteConsole(ix+" "+iy+";");
            }
        return holm;
    }
    private static byte[][] CreateObriv(int size_h, int hei_)
    {
        int x_cof = 0;
        int y_cof = 0;
        Random rand = new Random();
        if(rand.nextInt(1)==1)
            x_cof = size_h;
        if(rand.nextInt(1)==1)
            y_cof = size_h;

        byte[][] holm = new byte[size_h][size_h];
        for (short ix = 0; ix < size_h; ix++)
            for (short iy = 0; iy < size_h; iy++)
            {

                double vis = ((ix -x_cof) * (ix -x_cof)) +
                        ((iy-y_cof) * (iy-y_cof ));

                vis = (Math.sqrt(vis));

                if(vis>size_h) vis = 0;

                if (vis >= hei_) vis = hei_;
                if (vis <= 0) vis = 0;

                vis = Math.pow((vis / hei_), 2 - ((vis / hei_) * 1.9)) * hei_;


                holm[ix][iy] = (byte)vis;

                //Log.WriteConsole(ix+" "+iy+";");
            }
        return holm;
    }
    private static byte[][] AddToHeiMap(byte[][] heights_map, byte[][] holm, int count)
    {
        Random rand = new Random();
        int size_h = holm.length;
        ///Add_Holms
        //World.Inst().TextMessage("h " + World.Inst().GetLSize());
        for (int i = 0; i < count; i++)
        {
            int pos_x = rand.nextInt(heights_map.length - size_h - size_h - 10)+size_h;
            int pos_y = rand.nextInt(heights_map.length - size_h - size_h - 10)+size_h;

            boolean is_plus = true;

            if ((new Random()).nextInt(2) == 1)
                is_plus = false;
            else
                is_plus = true;

            for (short ix = 0; ix < size_h; ix++)
                for (short iy = 0; iy < size_h; iy++)
                {

                    double vis = heights_map[ix + pos_x][ iy + pos_y];
                    if (is_plus)
                        vis += holm[ix][ iy];
                    else
                        vis -= holm[ix][ iy];

                    if (vis > 126) vis = 126;
                    if (vis < 0) vis = 0;

                    //World.Inst().TextMessage("h " + vis);

                    heights_map[ix + pos_x][ iy + pos_y] = (byte)vis;
                }
        }
        return heights_map;
    }


    private static Enums.CellTps[][] GenerateTpsMap(byte[][] heights_map)
    {
        Enums.CellTps[][] tps_map = new Enums.CellTps[heights_map.length][heights_map.length];
        Random rand = new Random();
        for (int ix = 0; ix < tps_map.length; ix++)
            for (int iy = 0; iy < tps_map[ix].length; iy++)
            {
                tps_map[ix][iy] = Enums.CellTps.Dirt;
                if(heights_map[ix][iy] > Const.see_level)
                {
                    tps_map[ix][iy] = Enums.CellTps.Grass;


                    byte delta = 0;

                    if (Math.abs(heights_map[ix][iy] - heights_map[ix + 1][ iy - 1]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][iy] - heights_map[ix + 1][iy - 1]);

                    if (Math.abs(heights_map[ix][ iy] - heights_map[ix - 1][ iy - 1]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][ iy] - heights_map[ix - 1][ iy - 1]);

                    if (Math.abs(heights_map[ix][ iy] - heights_map[ix + 1][ iy + 1]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][ iy] - heights_map[ix + 1][ iy + 1]);

                    if (Math.abs(heights_map[ix][ iy] - heights_map[ix - 1][ iy + 1]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][ iy] - heights_map[ix - 1][ iy + 1]);



                    if (Math.abs(heights_map[ix][ iy] - heights_map[ix + 1][ iy]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][ iy] - heights_map[ix + 1][ iy]);

                    if (Math.abs(heights_map[ix][ iy] - heights_map[ix - 1][ iy]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][ iy] - heights_map[ix - 1][ iy]);

                    if (Math.abs(heights_map[ix][ iy] - heights_map[ix][ iy + 1]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][ iy] - heights_map[ix][ iy + 1]);

                    if (Math.abs(heights_map[ix][ iy] - heights_map[ix][ iy - 1]) > delta)
                    delta = (byte)Math.abs(heights_map[ix][ iy] - heights_map[ix][ iy - 1]);

                    if (delta < 6 && heights_map[ix][ iy] > Const.see_level + 2)
                    {
                        switch (rand.nextInt(19)+1)
                        {
                            case 1:
                                tps_map[ix][ iy] = Enums.CellTps.Tree;
                                break;
                            case 2:
                                tps_map[ix][ iy] = Enums.CellTps.TreeEll;
                                break;
                            case 3:
                                tps_map[ix][ iy] = Enums.CellTps.TreeSosna;
                                break;

                        }
                    }

                    if (delta > 0)
                    {



                        if (delta >= 6)
                        {
                            if (rand.nextInt(3) == 1)
                                tps_map[ix][ iy] = Enums.CellTps.Stone;
                            if (rand.nextInt(3) == 1)
                                tps_map[ix][ iy] = Enums.CellTps.Coal;
                            if (rand.nextInt(3) == 1)
                                tps_map[ix][ iy] = Enums.CellTps.Iron;

                        }
                    }
                    else
                    {

                    }
                 }
            }

        return tps_map;
    }

    public static LandObject[][] GenerateLandMatrix(short size)
    {
        LandObject[][] Land_Matrix = new LandObject[size][size];

        byte[][] heights_map = GenerateHeightsMap(size);

        Enums.CellTps[][] tps_map = GenerateTpsMap(heights_map);

        for (short ix = 0; ix < size; ix++)
            for (short iy = 0; iy < size; iy++)
                Land_Matrix[ix][iy] = new LandObject(heights_map[ix][iy], tps_map[ix][iy], ix,iy);


        return Land_Matrix;
    }
    public static void GenerateAnimals(short size, int anim_count)
    {
        Random rnd = new Random();
        rnd.setSeed(rnd.nextInt(1000));




        for (int i = 0; i < anim_count; i++)
        {
            try {
                Thread.sleep(1);
                rnd.nextDouble();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double x =     ((rnd.nextDouble() * (((double)size)/2d))+size/4) * Const.meters_in_cell_xy;
            double y =     ((rnd.nextDouble() * (((double)size)/2d))+size/4) * Const.meters_in_cell_xy;

           // if(i<10)
            //    World.Inst().TextMessage("X=" + x + "   Y=" + y);

            new AnimalObject((byte)0,new Vec3d(
                    //((Math.random() * (((double)size)/2d))+size/4) * Const.meters_in_cell_xy,
                    x,
                    y,

                    //(double)(rnd.nextInt(size/2)+size/4) * Const.meters_in_cell_xy,
                    //(double)(rnd.nextInt(size/2)+size/4) * Const.meters_in_cell_xy,
                    0));

        }
    }

    public static List<CharacterObject> GetConnectedPlayers(short x_centr_cell, short y_centr_cell, short dist_radius)
    {
        List<CharacterObject> list = new ArrayList<CharacterObject>();

        for(int ix = x_centr_cell - dist_radius; ix < x_centr_cell + dist_radius; ix++)
            for(int iy = y_centr_cell - dist_radius; iy < y_centr_cell + dist_radius; iy++)
            {
            LandObject cell1 =  World.Inst().GetLandCell((short) ix, (short) iy);
                if(cell1 != null)
                {
                    List<BaseObject> lbase = cell1.GetConteiner();

                    for(BaseObject ob : lbase)
                        if((ob instanceof CharacterObject) && ((CharacterObject)ob).IsConnect())
                            list.add((CharacterObject)ob);
                }
            }

        return list;
    }



}

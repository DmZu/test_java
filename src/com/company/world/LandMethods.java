package com.company.world;

import com.company.global.Const;
import com.company.global.Enums;
import com.company.world.objects.AnimalObject;
import com.company.world.objects.BaseObject;
import com.company.world.objects.CharacterObject;
import com.company.world.objects.LandObject;
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

        return heights_map;
    }



    private static Enums.CellTps[][] GenerateTpsMap(byte[][] heights_map)
    {
        Enums.CellTps[][] tps_map = new Enums.CellTps[heights_map.length][heights_map.length];

        for (int ix = 0; ix < tps_map.length; ix++)
            for (int iy = 0; iy < tps_map[ix].length; iy++)
            {
                tps_map[ix][iy] = Enums.CellTps.Dirt;
                if(heights_map[ix][iy] > Const.see_level)
                    tps_map[ix][iy] = Enums.CellTps.Grass;
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
            List<BaseObject> lbase = World.Inst().GetLandCell((short)ix, (short)iy).GetConteiner();

            for(BaseObject ob : lbase)
                if((ob instanceof CharacterObject) && ((CharacterObject)ob).IsConnect())
                    list.add((CharacterObject)ob);
            }

        return list;
    }



}

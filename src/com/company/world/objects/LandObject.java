package com.company.world.objects;

import com.company.global.Const;
import com.company.global.Enums;
import com.company.world.LandMethods;
import com.company.world.World;
import com.sun.javafx.geom.Vec3d;

import java.util.List;

/**
 * Created by Людмила on 06.01.14.
 */
public class LandObject extends BaseObject {

    private byte height = 0;
    private Enums.CellTps type;

    private double timer = 0;

    private static double time_for_grass_grow = 10;
    private static double time_for_tree_grow = 20;

    public LandObject(byte height_, Enums.CellTps type_, short ix, short iy)
    {
        super(ix,iy);
        height = height_;

        SetType(type_);
    }

    @Override
    protected void Work(double time_sec)
    {
        //Console.WriteLine(time_sec);
        timer += time_sec;

        if (height > Const.see_level + 2)
        {

            if (type == Enums.CellTps.Dirt && timer > time_for_grass_grow)
            {
                int delta_height = 0;



                for (short ix = (short)(GetCellX() - 1); ix < (short)(GetCellX() + 2); ix++)
                    for (short iy = (short)(GetCellY() - 1); iy < (short)(GetCellY() + 2); iy++)
                    {

                        if (Math.abs(GetHeight() - World.Inst().GetLandCell(ix,iy).GetHeight()) > delta_height)
                            delta_height = Math.abs(GetHeight() - World.Inst().GetLandCell(ix,iy).GetHeight());

                    }



                if (delta_height < 3)
                    World.Inst().GetLandCell(GetCellX(),GetCellY()).SetType(Enums.CellTps.Grass);
                timer = 0;
            }


            if (type == Enums.CellTps.Grass && timer > time_for_grass_grow)
            {

                boolean is_tree = false;

                for (short ix = (short)(GetCellX() - 1); ix < (short)(GetCellX() + 2); ix++)
                    for (short iy = (short)(GetCellY() - 1); iy < (short)(GetCellY() + 2); iy++)
                    {
                        if (World.Inst().GetLandCell(ix,iy).GetType() == Enums.CellTps.Tree)
                            is_tree = true;

                    }

                if (is_tree)
                    World.Inst().GetLandCell(GetCellX(),GetCellY()).SetType(Enums.CellTps.Grass);
                timer = 0;
            }

        }

    }

    public byte GetHeight()
    {
        return height;
    }

    public Enums.CellTps GetType()
    {
        return type;
    }

    public void SetType(Enums.CellTps t)
    {
        type = t;

        ClearMaterial();

        if (type == Enums.CellTps.Grass)
        {
            AddMaterial(Enums.GMaterials.Grass, 1000);
            AddMaterial(Enums.GMaterials.Woter, 1000);
        }

        ChangeCell();
    }

    private void ChangeCell()
    {
        List<CharacterObject> list = LandMethods.GetConnectedPlayers(GetCellX(), GetCellY(), (short) (Const.kvadrat_size * 2));

        for(CharacterObject ob : list)
            ob.GetTcpClient().SendCell(this);
    }

    public Vec3d GetPos()
    {
        Vec3d pos = new Vec3d(GetCellX() * Const.meters_in_cell_xy, GetCellY() * Const.meters_in_cell_xy, this.GetHeight() * Const.meters_in_cell_z);

        return pos;
    }
}

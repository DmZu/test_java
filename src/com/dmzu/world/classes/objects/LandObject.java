package com.dmzu.world.classes.objects;

import com.dmzu.world.classes.LandMethods;
import com.dmzu.world.classes.World;
import com.dmzu.world.classes.objects.abstr.BaseObject;
import com.dmzu.world.classes.types.Enums;
import com.dmzu.world.classes.types.Vec3d;

import java.util.List;

/**
 * Created by Людмила on 06.01.14.
 */
public class LandObject extends BaseObject {

    private short height = 0;
    private Enums.CellTps type;

    private double timer = 0;

    private static double time_for_grass_grow = 10;
    private static double time_for_tree_grow = 20;

    public LandObject(short height_, Enums.CellTps type_, short ix, short iy)
    {
        super(ix,iy);
        SetHeight(height_);
        type = type_;

        //SetType(type_);
    }

    @Override
    protected void Work(double time_sec)
    {
        //Console.WriteLine(time_sec);
        timer += time_sec;

        if (height > World.Inst().GetPropertes().get_See_level() + 2)
        {

            if (type == Enums.CellTps.Dirt && timer > time_for_grass_grow)
            {
                int delta_height = 0;



                for (short ix = (short)(GetCellX() - 1); ix < (short)(GetCellX() + 2); ix++)
                    for (short iy = (short)(GetCellY() - 1); iy < (short)(GetCellY() + 2); iy++)
                    {

                        if (Math.abs(GetHeightInByte() - World.Inst().GetLandCell(ix,iy).GetHeightInByte()) > delta_height)
                            delta_height = Math.abs(GetHeightInByte() - World.Inst().GetLandCell(ix,iy).GetHeightInByte());

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

    public byte GetHeightInByte()
    {
        return (byte)(height /*- Byte.MIN_VALUE*/);
    }

    public short GetHeight()
    {
        return height;
    }

    private void SetHeight(short h)
    {

        height = h;
        if(height < 0)
            height = 0;
        if(height > 255)
            height = 255;
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
        List<CharacterObject> list = LandMethods.GetConnectedPlayers(GetCellX(), GetCellY(), (short) (World.Inst().GetPropertes().get_Kvadrat_size() * 2));

        for(CharacterObject ob : list)
            ob.GetTcpClient().SendCell(this);
    }

    public Vec3d GetPos()
    {
        Vec3d pos = new Vec3d(GetCellX() * World.Inst().GetPropertes().get_Meters_in_cell_xy(),
                GetCellY() * World.Inst().GetPropertes().get_Meters_in_cell_xy(),
                this.GetHeightInByte() * World.Inst().GetPropertes().get_Meters_in_cell_z());

        return pos;
    }
}

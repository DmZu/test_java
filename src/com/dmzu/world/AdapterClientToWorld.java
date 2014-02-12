package com.dmzu.world;

import com.dmzu.world.classes.World;
import com.dmzu.world.classes.types.WorldPropertes;
import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.objects.LandObject;

/**
 * Created by d.zhukov on 08.02.14.
 */
public class AdapterClientToWorld {

    public static byte GetDayTimeNow()
    {
        return World.Inst().GetDayTimeNow();
    }

    public static void TextMessage(String message)
    {
        World.Inst().TextMessage(message);
    }

    public static byte GetSeeLevel()
    {
        return World.Inst().GetPropertes().get_See_level();
    }

    public static short GetSize()
    {
        return World.Inst().GetPropertes().get_Size();
    }

    public static int GetKvadratSize()
    {
        return World.Inst().GetPropertes().get_Kvadrat_size();
    }

    public static double GetMetersInCellXY()
    {
        return World.Inst().GetPropertes().get_Meters_in_cell_xy();
    }

    public static byte GetMetersInCellZ()
    {
        return World.Inst().GetPropertes().get_Meters_in_cell_z();
    }

    WorldPropertes GetPropertes();

    LandObject GetLandCell(short ix, short iy);



    CharacterObject GetChar(String name);


}

package com.dmzu.world;

import com.dmzu.world.classes.World;
import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.objects.abstr.DinamicObject;
import com.dmzu.world.classes.objects.abstr.StaticObject;
import com.dmzu.world.classes.types.ObjectID;
import com.dmzu.world.classes.types.Vec3d;

import java.nio.ByteBuffer;

/**
 * Created by d.zhukov on 08.02.14.
 */
public class AdapterToWorld {


    public static byte GetDayTimeNow()
    {
        return World.Inst().GetDayTimeNow();
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


    public static double GetMetersInCellZ()
    {
        return World.Inst().GetPropertes().get_Meters_in_cell_z();
    }


    public static byte[] GetLandCellBytes(short ix, short iy)
    {
        byte[] buf = new byte[2];

        if(World.Inst().GetLandCell(ix,iy) != null)
        {
            buf[0] = World.Inst().GetLandCell(ix,iy).GetHeightInByte();
            buf[1] = World.Inst().GetLandCell(ix,iy).GetType().GetByteVal();
        }

        return buf;
    }

    public static int GetCharID(String autoriz_str)
    {
        CharacterObject ob = World.Inst().GetChar(autoriz_str.split("\n")[0]);
        if(ob!=null)
            if(ob.IsPass(autoriz_str.split("\n")[1]))
                return ob.GetID();
            else
                return -1;
        else
            return -1;
    }



    public static byte[] GetObjPosByteBuff(int id)
    {
        StaticObject ob = ObjectID.GetObject(id);

        if(ob == null)
            return new byte[0];

        ByteBuffer bbuf;


        if(ob instanceof DinamicObject)
            bbuf = ByteBuffer.allocate(Vec3d.double_size*3);
        else
            bbuf = ByteBuffer.allocate(Vec3d.double_size*2);

        bbuf.put(ob.GetPos().GetDoubleByteBuffer());
        bbuf.put(ob.GetLookVec().GetDoubleByteBuffer());


        if(ob instanceof DinamicObject)
            bbuf.put(((DinamicObject)ob).GetVelVec().GetDoubleByteBuffer());

        return bbuf.array();
    }

    public static short GetCellX(int obj_id)
    {
        StaticObject ob = ObjectID.GetObject(obj_id);
        if(ob != null)
            return ob.GetCellX();
        else
            return Short.MIN_VALUE;
    }

    public static short GetCellY(int obj_id)
    {
        StaticObject ob = ObjectID.GetObject(obj_id);
        if(ob != null)
            return ob.GetCellY();
        else
            return Short.MIN_VALUE;
    }

    public static void SetCharLookTo(int char_id, byte[] data)
    {
        StaticObject ob = ObjectID.GetObject(char_id);
        if(ob != null && ob instanceof CharacterObject)
        {
            ByteBuffer buf = ByteBuffer.wrap(data);

            ((CharacterObject) ob).LookTo(
                    new Vec3d(
                            buf.getDouble(0),
                            buf.getDouble(8),
                            buf.getDouble(16)
                    )
            );
        }
    }

    public static void SetCharMove(int char_id, byte[] data)
    {
        StaticObject ob = ObjectID.GetObject(char_id);
        if(ob != null && ob instanceof CharacterObject)
        {
            ByteBuffer buf = ByteBuffer.wrap(data);

            ((CharacterObject) ob).Move(buf.getDouble());
        }
    }

    public static boolean RegistrNewChar(String registr_str)
    {
        if(World.Inst().GetChar(registr_str.split("\n")[0]) == null)
        {
            new CharacterObject((byte)0, new Vec3d(0,0,0), registr_str.split("\n")[0], registr_str.split("\n")[1]);
            return true;
        }
        else
            return false;
    }

    public static void TextMessage(String message)
    {
        World.Inst().TextMessage(message);
    }


}
package com.company.world.objects;

import com.company.global.Const;
import com.company.global.Enums;
import com.company.global.MaterialMass;
import com.company.world.World;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Людмила on 06.01.14.
 */
public abstract class BaseObject {

    private short iCell_x = 0, iCell_y = 0;

    private List<MaterialMass> list_mat = new ArrayList<MaterialMass>();

    private List<BaseObject> container = new ArrayList<BaseObject>();

    long time_last = System.currentTimeMillis();

    private long last_time = 0;

    private double tick_time_sek;

    boolean is_destroy = false;

    public BaseObject(short ix, short iy)
    {


        iCell_x = ix;
        iCell_y = iy;

        if(this instanceof StaticObject)
            World.Inst().GetLandCell(GetCellX(),GetCellY()).AddToContainer(this);

    }

    public void OneTick()
    {
        double time_sec = 0;
        if (last_time != 0)
            time_sec = (System.currentTimeMillis() - last_time)/1000.0;

        if (time_sec == 0) time_sec = 0.001;

        tick_time_sek = time_sec;



        Work(time_sec);


        last_time = System.currentTimeMillis();
    }

    protected abstract void Work(double time_sec);

    public double GetTickTimeSec()
    {
        return tick_time_sek;
    }
    protected void Destroy()
    {
        //AllObjects.Destroy(this);
        is_destroy = true;
    }
    public boolean IsDestroy()
    {
        return is_destroy;
    }

    protected void SetCell()
    {
        if (this instanceof  StaticObject)
        {
            Vec3d pos =  ((DinamicObject)this).GetPos();
            SetCell((short) (pos.x / Const.meters_in_cell_xy), (short) (pos.y / Const.meters_in_cell_xy));
        }
    }

    private void SetCell(short ix, short iy)
    {

        if (this instanceof  DinamicObject)
        {
            int i = 0;

            if(Math.abs(iCell_x - ix) > Math.abs(iCell_y - iy))
                i = (int)Math.abs(iCell_x - ix);
            else
                i = (int)Math.abs(iCell_y - iy);


            ((DinamicObject)this).AddWork(i);
        }


        World.Inst().GetLandCell(GetCellX(),GetCellY()).RemoveFromContainer(this);
        iCell_x = ix;
        iCell_y = iy;
        World.Inst().GetLandCell(GetCellX(),GetCellY()).AddToContainer(this);
    }

    public short GetCellX()
    {
        return iCell_x;
    }

    public short GetCellY()
    {
        return iCell_y;
    }

    public void AddToContainer(BaseObject obj)
    {
        container.add(obj);

    }

    public void RemoveFromContainer(BaseObject obj)
    {
        container.remove(obj);
    }

    public List<BaseObject> GetConteiner()
    {
        return container;
    }

    protected int GetMass()
    {
        int i = 0;
        for (MaterialMass element : list_mat)
        i += element.GetCount();

        for (BaseObject element : container)
        i += element.GetMass();

        return i;
    }

    public int GetMatCountByType(Enums.GMaterials mat_type )
    {
        int i = 0;

        for (MaterialMass element : list_mat)
        if(element.GetMatType() == mat_type)
            i += element.GetCount();

        return i;
    }

    public int RemoveMaterial(Enums.GMaterials mat_type, int count)
    {

        for (int i = 0; i < list_mat.size(); i++ )
            if (list_mat.get(i).GetMatType() == mat_type)
            {
                if (count >= list_mat.get(i).GetCount())
                {
                    count -= list_mat.get(i).GetCount();

                    list_mat.remove(i);
                    if (i > 0) i--;
                }
                else
                {

                    list_mat.set(i, new MaterialMass(
                            list_mat.get(i).GetMatType(),
                            (list_mat.get(i).GetCount() - count)
                    ));


                    count = 0;
                }
            }

        ///Kolvo ne udalennogo
        return count;
    }

    protected short ClearMaterial()
    {
        list_mat.clear();
        return 1;
    }

    public int AddMaterial(Enums.GMaterials mat_type, int count)
    {


        for (int i = 0; i < list_mat.size(); i++)
            if (list_mat.get(i).GetMatType() == mat_type)
            {
                    list_mat.set(i, new MaterialMass(list_mat.get(i).GetMatType(),  list_mat.get(i).GetCount() + count));
                    count = 0;
            }

        if(count > 0)
        {
            list_mat.add(new MaterialMass(mat_type, count));
            count = 0;
        }

        return 1;
    }
}

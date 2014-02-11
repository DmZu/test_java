package com.dmzu.world.classes.types;

/**
 * Created by Людмила on 07.01.14.
 */
public class MaterialMass
{
    private Enums.GMaterials matType;
    private int count;

    public Enums.GMaterials GetMatType()
    {
        return matType;
    }


    public int GetCount()
    {
        return count;
    }

    public MaterialMass(Enums.GMaterials type, int _count)
    {

        matType = type;

        count = _count;
    }



}

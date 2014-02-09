package com.dmzu.console.classes;

/**
* Created by d.zhukov on 08.02.14.
*/
public enum EnumCmdToWorld
{
    unknown((byte)-1),

    exit((byte)0),
    create((byte)1),
    start((byte)2),
    ticks((byte)3);

    private byte value;


    EnumCmdToWorld(byte val) {
        this.value = val;
    }

    public byte GetByteVal()
    {
        return value;
    }
/*
    public String GetName()
    {
        return name;
    }*/

    public static EnumCmdToWorld GetCmdByString(String str)
    {
       if(str.length() == 0)
           return unknown;
       for(EnumCmdToWorld ob : EnumCmdToWorld.values())
           if(ob.name().equals(str.toLowerCase()) || ob.name().charAt(0) == str.toLowerCase().charAt(0))
               return ob;


        return unknown;
    }

}

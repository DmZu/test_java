package com.dmzu.world.classes.types;

/**
 * Created by d.zhukov on 11.02.14.
 */
public class WorldPropertes {

    private double meters_in_cell_xy = 5.0;

    private double meters_in_cell_z = 1.0;

    private byte see_level = 10;

    private int kilogram_size = 1000;

    private int kvadrat_size = 64;

    private short size = 0;

    public WorldPropertes(short size_)
    {
        size = size_;
    }


    public double get_Meters_in_cell_xy() {
        return meters_in_cell_xy;
    }

    public double get_Meters_in_cell_z() {
        return meters_in_cell_z;
    }

    public byte get_See_level() {
        return see_level;
    }

    public int get_Kilogram_size() {
        return kilogram_size;
    }

    public int get_Kvadrat_size() {
        return kvadrat_size;
    }

    public short get_Size() {
        return size;
    }

}

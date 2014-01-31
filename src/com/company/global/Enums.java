package com.company.global;

/**
 * Created by Людмила on 06.01.14.
 */
public class Enums {

    public enum GMaterials
    {
        //Plants
        Wood((byte)0),
        Grass((byte)1),

        //Organic
        Bone((byte)2),
        Meat((byte)3),
        Fat((byte)4),
        Skinn((byte)5),
        Coat((byte)6),

        //Miniral
        Dirt((byte)7),
        Woter((byte)8),
        Stone((byte)9),
        Iron((byte)10),
        Coal((byte)11),
        Cuprum((byte)12);

        private byte value;

        private GMaterials(byte val) {
            this.value = val;
        }

        public byte GetByteVal()
        {
            return value;
        }
    }

    public enum CellTps
    {
        Dirt((byte)0),
        Grass((byte)1),
        TreeSosna((byte)2),
        Tree((byte)3),
        TreeEll((byte)4),

        Stone((byte)5),
        Coal((byte)6),
        Iron((byte)7);

        private byte value;

        private CellTps(byte val) {
            this.value = val;
        }
        public byte GetByteVal()
        {
            return value;
        }

    }


}

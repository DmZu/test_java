package com.company.global;

import java.nio.ByteBuffer;

/**
 * Created by dm on 26.01.14.
 */
public class Cmd {



    public enum CmdsToWorld
    {
        unknown((byte)-1),

        exit((byte)0),
        create((byte)1),
        start((byte)2),
        ticks((byte)3);

        private byte value;


        private CmdsToWorld(byte val) {
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

        public static CmdsToWorld GetCmdByString(String str)
        {

           for(CmdsToWorld ob : CmdsToWorld.values())
               if(ob.name().equals(str.toLowerCase()))
                   return ob;


            return unknown;
        }

    }


    ///TCP commands

    public enum TcpClient
    {
        Version ((byte)-127, (short)9),

        AutorizCli ((byte)-126, (short)81),

        RegistrCli ((byte)-125, (short)82),

        /// <summary>
        /// len 33
        /// X[8] Y[8] Z[8] AX[8] AY[8] AZ[8] VX[8] VY[8] VX[8]
        /// The position XY z_ A z_ VXY.
        /// </summary>
        PosXYZ_AXYZ_VXYZ ((byte)-105, (short)73),


        LandData ((byte)-104, (short)1025),

        CharMove ((byte)-103, (short)9),
        CharLookTo ((byte)-102, (short)9),

        unknown((byte)-128, (short)0);


        private byte val;
        private short len;


        private TcpClient(byte value, short length)
        {
            this.val = val;
            this.len = length;
        }

        public byte ToByte()
        {
            return val;
        }

        public short Length()
        {
            return len;
        }

        public static TcpClient GetCmdByByte(byte b)
        {
            for(TcpClient ob : TcpClient.values())
                if(ob.val == b)
                    return ob;
            return unknown;
        }

    }

}

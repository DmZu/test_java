package com.dmzu.type;

/**
 * Created by d.zhukov on 09.02.14.
 */
public class Vec3d {
    public double x;
    public double y;
    public double z;

    public Vec3d() { x=0;y=0;z=0; }

    public Vec3d(double v, double v1, double v2) { x=v;y=v1;z=v2; }

    public Vec3d(Vec3d vec3d) { x=vec3d.x;y=vec3d.y;z=vec3d.z; }

    public void set(Vec3d vec3d) { x=vec3d.x;y=vec3d.y;z=vec3d.z; }

    public void set(double v, double v1, double v2) { x=v;y=v1;z=v2; }

    public void mul(double v) { x*=v;y*=v;z*=v; }


    public void sub(Vec3d vec3d) { x -= vec3d.x; y -= vec3d.y; z -= vec3d.z; }

    public void add(Vec3d vec3d) { x += vec3d.x; y += vec3d.y; z += vec3d.z; }

    public double length() { return Math.sqrt((x * x) + (y * y) + (z * z)); }

    public void normalize() { x=x/length(); y=y/length(); z=z/length(); }

    //public void cross(com.dmzu.type.Vec3d vec3d, com.dmzu.type.Vec3d vec3d1) { /* compiled code */ }

    //public double dot(com.dmzu.type.Vec3d vec3d) { /* compiled code */ }

    //public int hashCode() { /* compiled code */ }

    public boolean equals(java.lang.Object o) {
        if((o instanceof Vec3d))
        {
            Vec3d v3 = (Vec3d)o;
            if(v3.x == x && v3.y == y && v3.z == z)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public java.lang.String toString() { return "" + x + "; " + y + "; " + z + "; "; }
}
package com.dmzu.world.classes.types;

import com.dmzu.world.classes.objects.abstr.LifeObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by d.zhukov on 22.01.14.
 */
public class LifePropertes {

    public String name = "def_name";
    public short day_vision = 5;
    public short night_vision = 1;

    public double can_walk = 1;
    public double can_clamber = 1;


    public List<Enums.GMaterials> mat_for_eat = new ArrayList<Enums.GMaterials>();

    private LifePropertes(LifeObject target_obj) { }

    public void AddMatForEat(Enums.GMaterials ml)
    {
        mat_for_eat.add(ml);
    }

    public static LifePropertes NewDefAnim(LifeObject target_obj)
    {

        LifePropertes la = new LifePropertes(target_obj);

        la.name = "def";
        la.day_vision = 30;
        la.night_vision = 5;

        la.AddMatForEat(Enums.GMaterials.Grass);


        target_obj.AddMaterial(Enums.GMaterials.Bone, 1000);
        target_obj.AddMaterial(Enums.GMaterials.Meat, 500);
        target_obj.AddMaterial(Enums.GMaterials.Skinn, 100);
        target_obj.AddMaterial(Enums.GMaterials.Coat, 100);
        target_obj.AddMaterial(Enums.GMaterials.Woter, 1000+ ((new Random()).nextInt(500)));
        target_obj.AddMaterial(Enums.GMaterials.Fat, 500+ ((new Random()).nextInt(500)));

        return la;

    }
}

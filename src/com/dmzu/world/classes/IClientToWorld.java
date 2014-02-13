package com.dmzu.world.classes;

import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.objects.LandObject;
import com.dmzu.world.classes.types.WorldPropertes;

/**
 * Created by d.zhukov on 13.02.14.
 */
public interface IClientToWorld {

    void TextMessage(String message);

    byte GetDayTimeNow();

    WorldPropertes GetPropertes();

    LandObject GetLandCell(short ix, short iy);

    CharacterObject GetChar(String name);

}

package com.dmzu.world;

import com.dmzu.world.classes.objects.CharacterObject;
import com.dmzu.world.classes.objects.LandObject;

/**
 * Created by d.zhukov on 08.02.14.
 */
public interface IClientToWorld {

    int GetLSize();

    LandObject GetLandCell(short ix, short iy);

    byte GetDayTimeNow();

    CharacterObject GetChar(String name);

    void TextMessage(String message);
}

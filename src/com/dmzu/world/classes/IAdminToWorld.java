package com.dmzu.world.classes;

import com.dmzu.console.IConsole;

/**
 * Created by Людмила on 06.01.14.
 */
public interface IAdminToWorld extends IClientToWorld
{
    void AddUI(IConsole ui);

    void Create(String name, short size);

    void Start(String name);

    void Stop();

    String GetTickTime();
}

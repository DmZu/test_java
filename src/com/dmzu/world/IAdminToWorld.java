package com.dmzu.world;

import com.dmzu.console.IConsole;

/**
 * Created by Людмила on 06.01.14.
 */
public interface IAdminToWorld extends AdapterClientToWorld
{
    void AddUI(IConsole ui);

    void Create(String name, short size);

    void Start(String name);

    void Stop();

    String GetTickTime();
}

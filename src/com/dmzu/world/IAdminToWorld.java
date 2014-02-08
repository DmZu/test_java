package com.dmzu.world;

/**
 * Created by Людмила on 06.01.14.
 */
public interface IAdminToWorld extends IClientToWorld
{
    void AddUI(IToAdmin ui);

    void Create(String name, short size);

    void Start(String name);

    void Stop();

    String GetTickTime();
}

package com.dmzu.console.classes;

import com.dmzu.Application;
import com.dmzu.console.IConsole;
import com.dmzu.world.classes.IAdminToWorld;
import com.dmzu.world.classes.World;

import java.util.Scanner;

/**
 * Created by Людмила on 02.01.14.
 */


public class Console_UI extends Thread implements IConsole
{
    private Scanner in;

    IAdminToWorld world = World.Inst();

    public Console_UI()
    {

        in = new Scanner(System.in);

        this.start();

        //String str = new String(ByteBuffer.allocate(80).array(), Charset.forName("UTF-16"));
        //String str = "111\n222\n333";
        //WriteLn("str = " + str.split("\n")[2]);
        WriteLn("App Version: " + Application.version);

        WriteLn("UI starting...");

        short i = 256;

        WriteLn("i="+(byte)i);

        world.AddUI(this);

        world.Create("1", (short) 1000);

        world.Start("1");
    }

    private void WriteLn(String str)
    {
        System.out.println(str);
    }

    private void Write(String str)
    {
        System.out.print(str);
    }
    private String ReadStringLn()
    {
        WriteLn("");
        Write("->");
        return in.nextLine();
    }

    @Override
    public void run() {


        while(true)
            ParsStr(ReadStringLn());

    }

    private void ParsStr(String str)
    {

        String name;
        short size;

        switch (EnumCmdToWorld.GetCmdByString(str))
        {
            case exit:
                WriteLn("Console thread finishing...");
                world.Stop();
                break;

            case create:
                Write("Enter world name: ");
                name = in.nextLine();
                Write("Enter world size: ");
                size = in.nextShort();
                world.Create(name, size);

                break;

            case start:
                Write("Enter world name: ");
                name = in.nextLine();
                world.Start(name);
                break;
            case ticks:
                Write(world.GetTickTime());
                break;

            default:
                WriteLn("Unknown command.");
        }
    }

    public void TextMessage(String mes)
    {
        WriteLn(mes);
    }

    public void DoString(String commanda)
    {
        ParsStr(commanda);
    }


}

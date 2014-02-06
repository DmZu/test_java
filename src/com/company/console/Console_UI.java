package com.company.console;

import com.company.global.Cmd;
import com.company.global.IAdmin;
import com.company.world.World;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Людмила on 02.01.14.
 */


public class Console_UI extends Thread implements IAdmin
{
    private Scanner in;


    public Console_UI()
    {

        in = new Scanner(System.in);

        this.start();

        //String str = new String(ByteBuffer.allocate(80).array(), Charset.forName("UTF-16"));
        //String str = "111\n222\n333";
        //WriteLn("str = " + str.split("\n")[2]);

        WriteLn("UI starting...");

        World.Inst().AddUI(this);

        World.Inst().Create("1", (short)1000);

        World.Inst().Start("1");
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
        System.out.print("->");
        return in.nextLine();
    }

    @Override
    public void run() {

        String name;
        short size;
        while(true)
        {
            switch (Cmd.CmdsToWorld.GetCmdByString(ReadStringLn()))
            {
                case exit:
                    WriteLn("Console thread finishing...");
                    return;

                case create:
                    Write("Enter world name: ");
                    name = in.nextLine();
                    Write("Enter world size: ");
                    size = in.nextShort();
                    World.Inst().Create(name, size);

                    break;

                case start:
                    Write("Enter world name: ");
                    name = in.nextLine();
                    World.Inst().Start(name);
                    break; 
                case ticks:
                    World.Inst().GetTickTime();
                    break;

                default:
                    WriteLn("Unknown command.");
            }

        }
    }

    public void TextMessage(String mes)
    {
        WriteLn(mes);
    }




}

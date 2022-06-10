package pl.poleq.discordoos.system;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class LogSystem
{
    public static void defaultLog(String msg)
    {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        System.out.println("[" + df.format(date) + "] " + msg + Color.RESET);
    }
}

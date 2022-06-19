package pl.poleq.discordoos;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import pl.poleq.discordoos.commands.Commands;
import pl.poleq.discordoos.database.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import pl.poleq.discordoos.events.Events;
import pl.poleq.discordoos.system.LogSystem;
//import pl.poleq.discordoos.twitchapi.TwitchAPI;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Odlaczeni
{
    public static JDA bot;
    public static JDABuilder builder;
    public static final String PREFIX = ";";
    public static final String SERVERID = "958640705507573770";

    private static DBApplications applications;
    private static DBChannels channels;
    private static DBActivity activity;
    private static DBActivityRewards activityRewards;
    private static DBActivityBoosts activityBoosts;
    private static DBVault vault;
    //    private static DBStreamers streamers;
//    private static TwitchAPI twitchAPI;

    public static void main(String[] args) throws LoginException, InterruptedException, IOException
    {
        System.out.println("uruchomiono program...");

        builder = JDABuilder.createDefault("NzgyMzQ2NDYwOTM0MTc2ODA5.Gkvh-9.k9SEIshHDCHAuPw-qTXSTRDx9wIaFOkZSCFVo8");
//        builder = JDABuilder.createDefault("OTc5MDkzMDMwNzcyNTYzOTY5.G2Wn2h.fj1bMGiXy5u0NhrNBRZUmYt94rPtjjfw-2d9co");
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setActivity(Activity.watching("serwer odłączonych"));

        bot = builder.build();
        System.out.println("oczekiwanie na załadowanie bota...");
        bot.awaitReady();
        System.out.println("bot załadowany!");

        // Tabele w bazie danych
        applications = new DBApplications();
        channels = new DBChannels();
        activity = new DBActivity();
        activityRewards = new DBActivityRewards();
        activityBoosts = new DBActivityBoosts();
//        streamers = new DBStreamers();
        vault = new DBVault();
//        twitchAPI = new TwitchAPI();

        // Komendy, Eventy
        new Commands(bot);
        new Events(bot);

        LogSystem.defaultLog("Bot Online!");
        System.out.println("started");

        stop();
    }

    public static void stop() throws IOException
    {
        while (true)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            if(reader.readLine().equals("stop"))
            {
                reader.close();
                builder.setStatus(OnlineStatus.OFFLINE);
                bot.shutdown();

                LogSystem.defaultLog("Bot wyłączony!");
                System.exit(0);
            }
        }
    }

    public static DBChannels getChannels() {
        return channels;
    }

    public static DBActivity getActivity() {
        return activity;
    }

    public static DBActivityRewards getActivityRewards() {
        return activityRewards;
    }

    public static DBActivityBoosts getActivityBoosts() {
        return activityBoosts;
    }

    public static DBApplications getApplications() {
        return applications;
    }

//    public static DBStreamers getStreamers() {
//        return streamers;
//    }

    public static DBVault getVault()
    {
        return vault;
    }
}

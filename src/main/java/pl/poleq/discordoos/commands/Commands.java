package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.JDA;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.util.ArrayList;
import java.util.List;

public class Commands
{
    private static final List<String> commands = new ArrayList<>();
    private static final List<String> usages = new ArrayList<>();
    private static final List<String> descriptions = new ArrayList<>();
    private static final List<String> adminCommands = new ArrayList<>();

    /**
     * Konstruktor "uruchamiajacy" wszystkie komendy
     * @param bot bot
     */
    public Commands(JDA bot)
    {
        bot.addEventListener(new CommandApplicationsChannel());
        bot.addEventListener(new CommandDeadChat());
        bot.addEventListener(new CommandErrorsChannel());
        bot.addEventListener(new CommandLvl());
        bot.addEventListener(new CommandPartnershipChannel());
        bot.addEventListener(new CommandPln());
        bot.addEventListener(new CommandPomoc());
        bot.addEventListener(new CommandRealizator());
        bot.addEventListener(new CommandSay());
        bot.addEventListener(new CommandSayChannel());
        bot.addEventListener(new CommandTemat());
        bot.addEventListener(new CommandTopLvl());
        bot.addEventListener(new CommandWelcomeChannel());
    }

    /**
     * Funkcja dodaje nowa komende do listy.
     * @param command komenda
     * @param usage uzycie
     * @param description opis komendy
     * @param adminCommand czy komenda jest przeznaczona dla administratorow
     */
    public static void addCommand(String command,String usage,String description,boolean adminCommand)
    {
        if(adminCommand)
            adminCommands.add(command);
        commands.add(command);
        usages.add(usage);
        descriptions.add(description);
    }

    /**
     * Funkcja usuwajaca komende/y z listy
     * @param command komenda
     * @param all jezeli jest wiecej niz 1, zostana usuniete wszystkie
     */
    @SuppressWarnings("all")
    public static void removeCommand(String command,boolean all)
    {
        if(all)
        {
            for(int i=0; i<commands.size() ;i++)
            {
                if(commands.get(i).equals(command))
                {
                    commands.remove(i);
                    usages.remove(i);
                    descriptions.remove(i);
                }
            }
        }

        int id = commands.indexOf(command);

        if(id == -1)
            return;

        commands.remove(id);
        usages.remove(id);
        descriptions.remove(id);
    }

    // Gettery
    public static List<String> getCommands() {
        return commands;
    }

    public static List<String> getUsages() {
        return usages;
    }

    public static List<String> getDescriptions() {
        return descriptions;
    }

    public static List<String> getAdminCommands() {
        return adminCommands;
    }
}

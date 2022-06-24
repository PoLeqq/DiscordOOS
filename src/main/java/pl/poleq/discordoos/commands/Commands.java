package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.JDA;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.util.ArrayList;
import java.util.List;

public class Commands
{
    private static final List<CommandTemplate> commands = new ArrayList<>();
    private static final List<CommandTemplate> adminCommands = new ArrayList<>();

    /**
     * Konstruktor "uruchamiajacy" wszystkie komendy
     * @param bot bot
     */
    public Commands(JDA bot)
    {
        // Zawsze admin pomoc jest na 1 miejscu
        bot.addEventListener(new CommandAdminPomoc());

        // Potem reszta komend
        bot.addEventListener(new CommandApplicationsChannel());
        bot.addEventListener(new CommandDeadChat());
        bot.addEventListener(new CommandErrorsChannel());
        bot.addEventListener(new CommandLvl());
//        bot.addEventListener(new CommandPartnershipChannel());
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
     */
    public static void addCommand(CommandTemplate command)
    {
        if(commands.contains(command))
            return;

        if(command.isAdminCommand())
            adminCommands.add(command);
        commands.add(command);
    }

    /**
     * Funkcja usuwajaca komende/y z listy
     * @param command komenda
     */
    @SuppressWarnings("all")
    public static void removeCommand(String command) {
        commands.remove(command);
    }

    // Gettery
    public static List<CommandTemplate> getCommands() {
        return commands;
    }

    public static List<CommandTemplate> getAdminCommands() {
        return adminCommands;
    }
}

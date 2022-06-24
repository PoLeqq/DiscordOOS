package pl.poleq.discordoos.logic;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.commands.Commands;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.Arrays;

public abstract class CommandTemplate extends ListenerAdapter
{
    protected final String COMMAND;
    protected final String[] ALIASES;
    protected final String USAGE;
    protected final String DESCRIPTION;
    protected final boolean ADMIN_COMMAND;
    protected Permissions requiredPermission;

    public CommandTemplate(String command, @Nullable String[] aliases, String usage, String description, boolean adminCommand)
    {
        // dodanie prefixów do komend
        COMMAND = Odlaczeni.PREFIX + command;
        if(aliases != null)
        {
            for(int i=0; i<aliases.length; i++)
            {
                aliases[i] = Odlaczeni.PREFIX + aliases[i].toLowerCase();
            }
            ALIASES = aliases;
        }
        else
            ALIASES = null;

        USAGE = Odlaczeni.PREFIX + usage;
        DESCRIPTION = description;
        ADMIN_COMMAND = adminCommand;

        if(!Commands.getCommands().contains(this))
            Commands.addCommand(this);
    }

    public CommandTemplate(String command, @Nullable String[] aliases, String usage, String description, boolean adminCommand, Permissions requiredPermission)
    {
        this(command,aliases,usage,description,adminCommand);

        this.requiredPermission = requiredPermission;
    }

    public boolean isCommand(MessageReceivedEvent e)
    {
        if(e.getAuthor().isSystem() || e.getAuthor().isBot())
            return false;

        String[] messageRaw = e.getMessage().getContentRaw().split(" ");

        if(messageRaw[0].equalsIgnoreCase(COMMAND))
            return true;

        if(ALIASES != null)
            return Arrays.stream(ALIASES).toList().contains(messageRaw[0]);

        return false;
    }

    public boolean isInGuild(MessageReceivedEvent e)
    {
        try { e.getGuildChannel(); } catch (Exception ex) { return false; }
        return true;
    }

    public String getCommand() {
        return COMMAND;
    }

    public String[] getAliases() {
        return ALIASES;
    }

    public String getUsage() {
        return USAGE;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public boolean isAdminCommand() {
        return ADMIN_COMMAND;
    }

    public Permissions getRequiredPermission() {
        return requiredPermission;
    }

    public boolean canMemberUse(Member member, @Nullable Permission permission)
    {
        if(member.hasPermission(Permission.ADMINISTRATOR))
            return true;

        if(member.hasPermission(permission))
            return true;

        if(requiredPermission != null) {
            try {
                if(Odlaczeni.getPermissions().hasPermission(member.getId(),Permissions.ALL))
                    return true;
                if(Odlaczeni.getPermissions().hasPermission(member.getId(),requiredPermission))
                    return true;
            } catch (SQLException e) {
                e.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się pobrać permisji użytkownika!",e);
            }
        }

        if(permission != null)
        {
            for(Role r : member.getRoles())
            {
                if(r.hasPermission(permission))
                    return true;
            }
        }

        return false;
    }

    public String[] getArgs(String message)
    {
        String mess = message.trim().replaceAll(" +"," ");
        if(mess.indexOf(' ') == -1)
            return new String[]{};
        mess = mess.substring(mess.indexOf(' ')+1);

        return mess.split(" ");
    }
}

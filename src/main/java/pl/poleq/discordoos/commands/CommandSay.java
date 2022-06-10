package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBChannels;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandSay extends ListenerAdapter implements CommandTemplate
{
    public final String COMMAND = Odlaczeni.PREFIX + "say";
    public final String USAGE = Odlaczeni.PREFIX + "say wiadomość`";
    public final String DESCRIPTION = Odlaczeni.PREFIX + "wysyła zadaną wiadomość (oraz usuwa wiadomość użytkownika)";

    public CommandSay()
    {
        Commands.addCommand(COMMAND,USAGE,DESCRIPTION,true);
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot())
            return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        MessageChannel mc = event.getChannel();

        if(!args[0].equalsIgnoreCase(COMMAND))
            return;

        if(!Objects.requireNonNull(event.getGuild().getMemberById(event.getAuthor().getId())).hasPermission(Permission.ADMINISTRATOR))
        {
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        if(args.length <= 1)
        {
            mc.sendMessage(MessageSystem.Errors.USAGE + USAGE).queue();
            return;
        }

        DBChannels channels = Odlaczeni.getChannels();
        MessageChannel sayChannel;
        try {
            sayChannel = (MessageChannel) Odlaczeni.bot.getGuildChannelById(channels.getChannelID(channels.SAY));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie znaleziono kanału "+channels.SAY,e);
            return;
        }

        // Ustawienie kanału (z BD), jezeli jest pusty - wyśilij tam, gdzie została wywołana komenda
        try {
            if(channels.getChannelID(channels.SAY) == 0) {
                mc.sendMessage(event.getMessage().getContentRaw()).queue();
                event.getMessage().delete().queue();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie znaleziono kanału "+channels.SAY,e);
            return;
        }

        if(sayChannel == null)
        {
            mc.sendMessage(MessageSystem.Errors.NOT_A_CHANNEL).queue();
            return;
        }
        sayChannel.sendMessage(event.getMessage().getContentRaw()).queue();
    }

    @Override
    public boolean usage(String[] args) {
        return false;
    }

    @Override
    public boolean args(String[] args) {
        return false;
    }

    @Override
    public boolean perms(String id, String permission) {
        return false;
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public List<String> allowedIds() {
        return null;
    }
}

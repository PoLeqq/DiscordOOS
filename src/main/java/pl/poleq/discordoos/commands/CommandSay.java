package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBChannels;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandSay extends CommandTemplate
{
    public CommandSay()
    {
        super("say",new String[]{},"say wiadomość","wysyła zadaną wiadomość (oraz usuwa wiadomość użytkownika)",true);
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isInGuild(event))
            return;
        if(!isCommand(event))
            return;

        String[] args = getArgs(event.getMessage().getContentRaw());
        MessageChannel mc = event.getChannel();

        if(!canMemberUse(Objects.requireNonNull(event.getMember()),Permission.ADMINISTRATOR))
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

        // Ustawienie kanału (z BD), jezeli jest pusty – wyśilij tam, gdzie została wywołana komenda
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
            mc.sendMessage(MessageSystem.Errors.CHANNEL_NOT_FOUND).queue();
            return;
        }
        sayChannel.sendMessage(event.getMessage().getContentRaw()).queue();
    }
}

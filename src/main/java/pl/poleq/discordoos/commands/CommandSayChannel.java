package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBChannels;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.ArgSystem;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandSayChannel extends CommandTemplate
{
    public CommandSayChannel()
    {
        super("saychannel",new String[]{},"saychannel <#kanał>` LUB `;saychannel null","ustawia kanał, na który ma zostać wysłana wiadomość",true);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isCommand(event))
            return;

        String[] args = getArgs(event.getMessage().getContentRaw());

        if(!canMemberUse(Objects.requireNonNull(event.getMember()),Permission.ADMINISTRATOR))
        {
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        MessageChannel mc = event.getChannel();
        if(args.length != 1)
        {
            mc.sendMessage(MessageSystem.Errors.USAGE + USAGE).queue();
            return;
        }

        // Jeżeli drugi argument to "reset", zresetuj kanał
        if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("restart") || args[0].equalsIgnoreCase("null"))
        {
            DBChannels channels = Odlaczeni.getChannels();
            try {
                channels.setChannel(channels.SAY,0);
            } catch (SQLException e) {
                e.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się ustawić kanału "+channels.SAY,e);
                return;
            }
            mc.sendMessage("Zresetowano kanał wysyłania wiadomości!").queue();
            return;
        }

        // Sprawdzenie drugiego argumentu, czy jest on faktycznie poprawny
        ArgSystem arg = new ArgSystem();
        GuildChannel targetChannel = arg.getChannel(args[0]);

        if(targetChannel == null)
        {
            mc.sendMessage(MessageSystem.Errors.CHANNEL_NOT_FOUND).queue();
            return;
        }

        DBChannels channels = Odlaczeni.getChannels();
        try {
            channels.setChannel(channels.SAY, Long.parseLong(targetChannel.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się ustawić kanału "+channels.SAY,e);
            return;
        }
        mc.sendMessage("Zmieniono kanal na <#" + targetChannel.getId()+">!").queue();
    }
}

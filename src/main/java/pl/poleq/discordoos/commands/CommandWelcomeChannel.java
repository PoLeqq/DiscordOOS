package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.events.OnJoin;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.ArgSystem;
import pl.poleq.discordoos.system.MessageSystem;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandWelcomeChannel extends CommandTemplate
{
    public CommandWelcomeChannel()
    {
        super("welcomechannel",new String[]{"wchannel"},"wchannel <#kanał>","ustawia kanał powitalny serwera",true);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isInGuild(event))
            return;
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
            OnJoin.setWelcomeChannel(0);
            mc.sendMessage("Zresetowano kanał powitalny!").queue();
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

        OnJoin.setWelcomeChannel(Long.parseLong(targetChannel.getId()));
        mc.sendMessage("Zmieniono kanał powitalny na <#" + targetChannel.getId()+">!").queue();
    }
}

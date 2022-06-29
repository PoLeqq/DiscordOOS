package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.poleq.discordoos.events.Partnerships;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.ArgSystem;
import pl.poleq.discordoos.system.MessageSystem;

import java.util.Objects;

public class CommandPartnershipChannel extends CommandTemplate
{
    public CommandPartnershipChannel()
    {
        super("partnershipschannep",new String[]{"pschannel"},"pschannel <#kanał>","ustawia kanał, na który jest odpowiedzialny za kanał do partnerstw międzyserwerowych",true);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isCommand(event))
            return;
        if(!isInGuild(event))
            return;
        if(!canMemberUse(Objects.requireNonNull(event.getMember()), Permission.ADMINISTRATOR))
            return;

        String[] args = getArgs(event.getMessage().getContentRaw());

        MessageChannel mc = event.getChannel();
        if(args.length != 1)
        {
            mc.sendMessage(MessageSystem.Errors.USAGE + USAGE + "`").queue();
            return;
        }

        // Jeżeli drugi argument to "reset", zresetuj kanał
        if(args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("restart") || args[0].equalsIgnoreCase("null"))
        {
            Partnerships.setPartnershipsChannel(0);
            mc.sendMessage("Zresetowano kanał partnerstw!").queue();
            return;
        }

        ArgSystem arg = new ArgSystem();
        GuildChannel targetChannel = arg.getChannel(args[0]);
        if(targetChannel == null)
        {
            mc.sendMessage(MessageSystem.Errors.CHANNEL_NOT_FOUND).queue();
            return;
        }

        Partnerships.setPartnershipsChannel(targetChannel.getIdLong());
        mc.sendMessage("Zmieniono kanal na <#" + targetChannel.getId()+">!").queue();
    }
}

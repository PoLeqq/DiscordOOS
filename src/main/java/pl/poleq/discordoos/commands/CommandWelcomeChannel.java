package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.events.Applications;
import pl.poleq.discordoos.events.OnJoin;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandWelcomeChannel extends ListenerAdapter implements CommandTemplate
{
    private final String COMMAND = Odlaczeni.PREFIX + "wchannel";
    private final String USAGE = Odlaczeni.PREFIX + "wchannel <#kanał>`";
    private final String DESCRIPTION = "ustawia kanał powitalny serwera";

    public CommandWelcomeChannel()
    {
        Commands.addCommand(COMMAND,USAGE,DESCRIPTION,true);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        String[] args = event.getMessage().getContentRaw().split(" ");

        if(!args[0].equalsIgnoreCase(COMMAND))
            return;

        if(!Objects.requireNonNull(event.getGuild().getMemberById(event.getAuthor().getId())).hasPermission(Permission.ADMINISTRATOR))
        {
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        MessageChannel mc = event.getChannel();
        if(args.length != 2)
        {
            mc.sendMessage(MessageSystem.Errors.USAGE + USAGE).queue();
            return;
        }

        // Jeżeli drugi argument to "reset", zresetuj kanał
        if(args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("restart") || args[1].equalsIgnoreCase("null"))
        {
            OnJoin.setWelcomeChannel(0);
            mc.sendMessage("Zresetowano kanał powitalny!").queue();
            return;
        }

        // Sprawdzenie drugiego argumentu, czy jest on faktycznie poprawny
        Pattern channelPattern = Pattern.compile("<#\\d{18}>");
        Matcher channelMatcher = channelPattern.matcher(args[1]);

        if(!channelMatcher.matches())
        {
            mc.sendMessage(MessageSystem.Errors.NOT_A_CHANNEL).queue();
            return;
        }

        String channelId = args[1].substring(2, args[1].length()-1);
        MessageChannel targetChannel = (MessageChannel) event.getGuild().getGuildChannelById(channelId);
        if(targetChannel == null)
        {
            mc.sendMessage(MessageSystem.Errors.CHANNEL_NOT_FOUND).queue();
            return;
        }

        OnJoin.setWelcomeChannel(Long.parseLong(targetChannel.getId()));
        mc.sendMessage("Zmieniono kanał powitalny na <#" + targetChannel.getId()+">!").queue();
    }


    public String getCommand(){
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

    public String getUsage(){
        return USAGE;
    }
}

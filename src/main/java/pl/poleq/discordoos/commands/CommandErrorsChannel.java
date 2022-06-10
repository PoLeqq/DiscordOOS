package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandErrorsChannel extends ListenerAdapter implements CommandTemplate
{
    private final String COMMAND = Odlaczeni.PREFIX + "erchannel";
    private final String USAGE = Odlaczeni.PREFIX + "erchannel <#kanał>`";
    private final String DESCRIPTION = "ustawia kanał, na który jest odpowiedzialny za kanał do wysyłania błędów";

    public CommandErrorsChannel()
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
            setErrorLogChannel(0);
            mc.sendMessage("Zresetowano kanał wysyłania wiadomości!").queue();
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

        setErrorLogChannel(Long.parseLong(targetChannel.getId()));
        mc.sendMessage("Zmieniono kanal na <#" + targetChannel.getId()+">!").queue();
    }

    private void setErrorLogChannel(long channelID)
    {
        DBChannels channels = Odlaczeni.getChannels();
        try {
            channels.setChannel("error", channelID);
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się zmienić kanału!",e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void logToChannel(String msg)
    {
        DBChannels channels = Odlaczeni.getChannels();
        MessageChannel channel;
        try {
            channel = Odlaczeni.bot.getGuildById(958640705507573770L).getTextChannelById(channels.getChannelID("error"));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel(e);
            return;
        }
        channel.sendMessage(msg).queue();
    }

    @SuppressWarnings("ConstantConditions")
    public static void logToChannel(MessageEmbed msg)
    {
        DBChannels channels = Odlaczeni.getChannels();
        MessageChannel channel;
        try {
            channel = Odlaczeni.bot.getGuildById(958640705507573770L).getTextChannelById(channels.getChannelID("error"));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel(e);
            return;
        }
        channel.sendMessageEmbeds(msg).queue();
    }

    @SuppressWarnings("ConstantConditions")
    public static void logToChannel(String msg, Exception e)
    {
        String description = msg + "\n ```" + e.getMessage() + "\n```";

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0xff0000);
        eb.setTitle("Błąd!");
        eb.setDescription(description);
        eb.setFooter("Naprawiaj poqelu, powodzenia :)");

        DBChannels channels = Odlaczeni.getChannels();
        MessageChannel channel;
        try {
            channel = Odlaczeni.bot.getGuildById(958640705507573770L).getTextChannelById(channels.getChannelID("error"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            CommandErrorsChannel.logToChannel(ex);
            return;
        }
        channel.sendMessageEmbeds(eb.build()).queue();
    }

    @SuppressWarnings("ConstantConditions")
    public static void logToChannel(Exception e)
    {
        String description = "Wystąpił błąd!\n ```" + e.getMessage() + "\n```";

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0xff0000);
        eb.setTitle("Błąd!");
        eb.setDescription(description);
        eb.setFooter("Naprawiaj poqelu, powodzenia :)");

        DBChannels channels = Odlaczeni.getChannels();
        MessageChannel channel;
        try {
            channel = Odlaczeni.bot.getGuildById(958640705507573770L).getTextChannelById(channels.getChannelID("error"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            CommandErrorsChannel.logToChannel(ex);
            return;
        }
        channel.sendMessageEmbeds(eb.build()).queue();
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

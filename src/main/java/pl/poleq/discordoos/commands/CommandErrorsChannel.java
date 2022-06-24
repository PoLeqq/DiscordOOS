package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

public class CommandErrorsChannel extends CommandTemplate
{
    public CommandErrorsChannel()
    {
        super("errorchannel",new String[]{"erchannel"},"erchannel <#kanał>","ustawia kanał, na który jest odpowiedzialny za kanał do wysyłania błędów",true);
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
            setErrorLogChannel(0);
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
}

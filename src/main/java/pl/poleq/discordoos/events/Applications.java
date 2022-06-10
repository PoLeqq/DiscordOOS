package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.database.DBApplications;
import pl.poleq.discordoos.database.DBChannels;
import pl.poleq.discordoos.system.Color;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Applications extends ListenerAdapter
{
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        DBChannels channels = Odlaczeni.getChannels();
        MessageChannel applicationChannel;
        try {
            applicationChannel = (MessageChannel) Odlaczeni.bot.getGuildChannelById(channels.getChannelID(channels.APPLICATIONS));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się pobrać kanału "+channels.APPLICATIONS,e);
            return;
        }

        if(applicationChannel == null)
        {
            LogSystem.defaultLog(Color.RED + "Nie znaleziono kanału podań!");
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Ostrzeżenie!");
            eb.setDescription("Nie znaleziono kanału podań!");
            eb.setColor(0xFFA500);
            CommandErrorsChannel.logToChannel(eb.build());
            return;
        }

        User user = event.getAuthor();

        if(!event.getChannel().getId().equals(applicationChannel.getId()))
            return;

        if(user.isBot() || user.isSystem())
            return;

        DBApplications applications = Odlaczeni.getApplications();
        event.getMessage().delete().queue();

        // Jeżeli użytkownik nigdy nie napisał podania (lub wystąpił błąd :c)
        try {
            if(applications.getLastApplication(user.getId()) == 0)
            {
                applications.insertApplication(user.getId(), event.getMessage().getContentRaw());

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle(user.getAsTag());
                eb.setDescription(event.getMessage().getContentRaw());
                eb.setImage(user.getAvatarUrl());
                eb.setFooter("ID użytkownika:" + user.getId());

                Objects.requireNonNull(event.getGuild().getTextChannelById(980179708811571251L))
                        .sendMessageEmbeds(eb.build()).queue();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się pobrać ostatniego podania użytkownika "+user.getAsMention(),e);
            return;
        }

        // Jeżeli napisał
        try {
            if(System.currentTimeMillis() - applications.getLastApplication(user.getId()) < 1209600000)
            {
                Objects.requireNonNull(event.getGuild().getTextChannelById(973990702159650887L))
                        .sendMessage("Hej, <@"+user.getId()+">, podania możesz wysyłać co 2 tygodnie!").queue((message) ->
                                message.delete().queueAfter(8, TimeUnit.SECONDS));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się pobrać ostatniego podania użytkownika "+user.getAsMention(),e);
            return;
        }

        try {
            applications.insertApplication(user.getId(),event.getMessage().getContentRaw());
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się dodać podania użytkownika "+user.getAsMention(),e);
            // NIE DAWAĆ RETURN'A!!!
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(user.getAsTag());
        eb.setDescription(event.getMessage().getContentRaw());
        eb.setImage(user.getAvatarUrl());
        eb.setFooter("ID użytkownika:" + user.getId());

        Objects.requireNonNull(event.getGuild().getTextChannelById(980179708811571251L))
                .sendMessageEmbeds(eb.build()).queue();
    }

    public static void setApplicationChannel(long channelID)
    {
        DBChannels channels = Odlaczeni.getChannels();
        try {
            channels.setChannel(channels.APPLICATIONS, channelID);
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się ustawić kanału "+channels.APPLICATIONS,e);
        }
    }
}

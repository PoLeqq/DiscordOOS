package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.database.DBChannels;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class OnJoin extends ListenerAdapter
{
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event)
    {
        DBChannels channels = Odlaczeni.getChannels();

        System.out.println("ktoś dołączył na serwer");

        MessageChannel mc;
        try {
            mc = event.getGuild().getTextChannelById(channels.getChannelID("welcome"));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się pobrać kanału powitalnego",e);
            return;
        }
        if(mc == null)
        {
            System.out.println("welcome channel to null!");
            return;
        }

        Guild server = event.getGuild();
        Member member = event.getMember();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Witaj "+member.getEffectiveName()+"!");
        eb.setDescription("Jesteś naszym **"+server.getMemberCount()+"** członkiem!\n"+
                "Koniecznie zajrzyj na kanał <#958640706040262672>!\n"+
                "Aby się zweryfikować, wciśnij reakcje pod wiadomością na kanale <#958640706040262668>");
        eb.setFooter("Mamy nadzieję, że zostaniesz z nami na dłużej!");
        User user = member.getUser();
        eb.setThumbnail(user.getAvatarUrl());
        System.out.println(user.getAvatarUrl());
        eb.setColor(0x00ee00);

        mc.sendMessageEmbeds(eb.build()).queue();
        mc.sendMessage("<@"+member.getId()+">").queue((message) ->
                message.delete().queueAfter(1, TimeUnit.SECONDS));
    }

    public static void setWelcomeChannel(long channelID)
    {
        DBChannels channels = Odlaczeni.getChannels();
        try {
            channels.setChannel("welcome", channelID);
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się ustawić kanału powitalnego",e);
        }
    }
}

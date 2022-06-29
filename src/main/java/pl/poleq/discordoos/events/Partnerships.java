package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.database.DBChannels;
import pl.poleq.discordoos.system.ArgSystem;

import java.sql.SQLException;

public class Partnerships extends ListenerAdapter
{
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        try{
            event.getGuildChannel();
        } catch (Exception ignore) { return; }

        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        DBChannels channels = Odlaczeni.getChannels();
        MessageChannel partnershipsChannel;
        try {
            partnershipsChannel = (MessageChannel) Odlaczeni.bot.getGuildChannelById(channels.getChannelID(channels.PARTNERSHIPS));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Błąd podczas pobierania kanału partnerstw",e);
            return;
        }

        if(partnershipsChannel == null)
            return;

        if(!event.getChannel().getId().equals(partnershipsChannel.getId()))
            return;

//        Txtconfiguration prod = new Txtconfiguration(Odlaczeni.getProducers().getFile());
//        String part = prod.getString(event.getAuthor().getId());
//        if(part == null)
//            part = "0";
//        int pships;
//        try{
//            pships = Integer.parseInt(part);
//        } catch (NumberFormatException e) {
//            event.getChannel().sendMessage(part + " to nie liczba!").queue();
//            return;
//        }
//        pships++;
//        prod.set(event.getAuthor().getId(), String.valueOf(pships));

        User partner = null;
        for(String s : event.getMessage().getContentRaw().split(" "))
        {
            ArgSystem arg = new ArgSystem();

            if(arg.getUser(s) != null)
            {
                partner = arg.getUser(s);
                break;
            }
        }

        String description = "**Dziękujemy realizatorowi** "+event.getAuthor().getAsMention()+" *za wykonane partnerstwo*\n" + //TODO "już x!" +
                "**oraz serwerowi** *za zawarcie partnerstwa*\n\n";

        if(partner != null)
            description += "Partner: <@"+partner.getId() + ">";
        else
            description += "Partner: `nie znaleziono :c`";

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0x00ffff);
        eb.setTitle("Nowe partnerstwo!");
        eb.setDescription(description);
        eb.setFooter("Serwerek pomalutku się rozwija c:");

        event.getChannel().sendMessage("Ping: <@&970983335612526622>").queue();
        event.getChannel().sendMessageEmbeds(eb.build()).queue();
    }

    public static void setPartnershipsChannel(long channelID)
    {
        DBChannels channels = Odlaczeni.getChannels();
        try {
            channels.setChannel(channels.PARTNERSHIPS, channelID);
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się zmienić kanał partnerstw!");
        }
    }
}

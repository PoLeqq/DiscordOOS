package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBChannels;
import pl.poleq.discordoos.system.Color;
import pl.poleq.discordoos.system.LogSystem;

import java.util.regex.Pattern;

public class Partnerships extends ListenerAdapter
{
//    @Override
//    public void onMessageReceived(@NotNull MessageReceivedEvent event)
//    {
//        DBChannels channels = Odlaczeni.getChannels();
//        MessageChannel partnershipsChannel = (MessageChannel) Odlaczeni.bot.getGuildChannelById(channels.getChannelID(channels.PARTNERSHIPS));
//
//        if(partnershipsChannel == null) {
//            LogSystem.defaultLog(Color.RED + "Nie znaleziono kanału partnerstw!");
//            return;
//        }
//
//        if(!event.getChannel().getId().equals(partnershipsChannel.getId()))
//            return;
//
//        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
//            return;
//
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
//
//        String[] msg = event.getMessage().getContentRaw().split(" ");
//        User partner = null;
//        for(String s : msg)
//        {
//            Pattern userPattern = Pattern.compile("<@[0-9]{18}>");
//
//            if(userPattern.matcher(s).matches())
//            {
//                partner = event.getGuild().getJDA().getUserById(s.substring(2,20));
//            }
//        }
//
//        String message = "**Nowe partnerstwo!**\n" +
//                "Ping: <@&979019999085735976>\n" +
//                "Wykonałeś " + pships + " partnerstw\n";
//
//        if(partner != null)
//            message += "Partner: <@"+partner.getId() + ">\n";
//        else
//            message += "Partner: `brak`\n";
//
//        event.getChannel().sendMessage(message).queue();
//    }
//
//    public static void setPartnershipsChannel(long channelID)
//    {
//        DBChannels channels = Odlaczeni.getChannels();
//        channels.setChannel(channels.PARTNERSHIPS, channelID);
//    }
}

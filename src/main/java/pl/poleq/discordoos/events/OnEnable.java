package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OnEnable
{
    public OnEnable(JDA bot)
    {
        MessageChannel channel = (MessageChannel) bot.getGuildChannelById("977304610018586694");

        if(channel == null)
        {
            LogSystem.defaultLog("Niepoprawne ID kanału do logów!");
            return;
        }

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Aktualizacja");
        embed.setDescription("Bot włączony!");
        embed.setFooter(df.format(date));
        embed.setThumbnail("https://poleq.pl/img/oos.png");
        embed.setColor(0x00ff00);

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}

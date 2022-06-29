package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Funfacts extends ListenerAdapter
{
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        try{
            event.getGuildChannel();
        } catch (Exception ingore) { return; }

        if(!event.getGuildChannel().getId().equals("986267753323307038"))
            return;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0x00ff00);
        eb.setTitle("Nowa ciekawostka!");
        eb.setDescription("Jeśli przeczytałeś/aś to daj o sobie znać klikając w reakcję pod wiadomością!");
        eb.setFooter("Dziękujemy Ci "+event.getAuthor().getName()+" za kolejną ciekawostkę :D");

        event.getChannel().sendMessageEmbeds(eb.build()).queue();
        event.getChannel().sendMessage("<@&987119937497731115>").queue((m) -> m.delete().queueAfter(1, TimeUnit.SECONDS));
        event.getMessage().addReaction("\uD83D\uDCAF").queue();
    }
}

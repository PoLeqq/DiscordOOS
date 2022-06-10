package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class OnPing extends ListenerAdapter
{
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!event.getMessage().getContentRaw().equals("<@782346460934176809>"))
            return;

        event.getChannel().sendMessage("Witaj "+event.getAuthor().getAsMention() +"! Aby poznać wszystkie komendy, wpisz `;pomoc`")
                .queue((message) -> message.delete().queueAfter(10, TimeUnit.SECONDS));
    }
}

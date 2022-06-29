package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.events.GatewayPingEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class AllEvents implements EventListener
{
    @Override
    public void onEvent(@NotNull GenericEvent genericEvent)
    {
        if(genericEvent instanceof MessageReceivedEvent)
            return;
        if(genericEvent instanceof GatewayPingEvent)
            return;
        System.out.println("Event:  " + genericEvent);
    }
}

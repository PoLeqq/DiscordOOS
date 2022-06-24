package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandRealizator extends CommandTemplate
{
    public CommandRealizator()
    {
        super("realizator",new String[]{},"realizator <@użytkownik>","wypisuje wszelkie informacje na temat realizatora",true);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isCommand(event))
            return;

        String[] msg = event.getMessage().getContentRaw().split(" ");
        if(!msg[0].equals(Odlaczeni.PREFIX + COMMAND))
            return;

        if(!Objects.requireNonNull(event.getGuild().getMemberById(event.getAuthor().getId())).hasPermission(Permission.ADMINISTRATOR))
        {
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
            //return;
        }

//        if(msg.length != 1)
//        {
//            event.getChannel().sendMessage();
//        }
//
//        User user = User.fromId(msg[1]);
//        if(user == null)
    }
}

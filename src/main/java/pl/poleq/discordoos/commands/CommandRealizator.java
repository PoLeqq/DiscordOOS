package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandRealizator extends ListenerAdapter implements CommandTemplate
{
    public static String COMMAND = Odlaczeni.PREFIX + "realizator";
    public static String USAGE = Odlaczeni.PREFIX + "realizator <@użytkownik>`";
    public static String DESCRIPTION = "wypisuje wszelkie informacje na temat realizatora";

    public CommandRealizator()
    {
        Commands.addCommand(COMMAND,USAGE,DESCRIPTION,true);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(event.getAuthor().isSystem() || event.getAuthor().isBot())
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

    @Override
    public boolean usage(String[] args) {
        return false;
    }

    @Override
    public boolean args(String[] args) {
        return false;
    }

    @Override
    public boolean perms(String id, String permission) {
        return false;
    }

    @Override
    public boolean isAdminCommand() {
        return true;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public List<String> allowedIds() {
        return null;
    }
}

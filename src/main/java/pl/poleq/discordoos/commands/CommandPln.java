package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBVault;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class CommandPln extends CommandTemplate
{
    public CommandPln()
    {
        super("pln",new String[]{},"pln` LUB `"+Odlaczeni.PREFIX + "pln @uzytkownik","pokazuje ilość gotówki w portfelu",false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isInGuild(event))
            return;
        if(!isCommand(event))
            return;

        String[] args = getArgs(event.getMessage().getContentRaw());

        User user = event.getAuthor();
        EmbedBuilder eb = new EmbedBuilder();

        if(args.length == 0)
        {
            String description;
            try{
                description = getEmbedDescription(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się pobrać gotówki użytkownika "+user.getId()+":",throwables);
                return;
            }

            eb.setThumbnail(user.getAvatarUrl());
            eb.setTitle("Portfel użytkownika " + user.getName());
            eb.setDescription(description);
            eb.setColor(0x00ff55);
        }
        else if(args.length == 1)
        {
            //TODO
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
        }

        event.getChannel().sendMessageEmbeds(eb.build()).queue();
    }

    private String getEmbedDescription(User user) throws SQLException
    {
        DBVault vault = Odlaczeni.getVault();
//        String pln = "**Pieniądze:** "+vault.getDisplayPln(user.getId()) + " PLN";
        return "**Pieniądze:** "+vault.getDisplayPln(user.getId()) + " PLN";
    }
}

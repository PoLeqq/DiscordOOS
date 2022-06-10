package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBVault;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandPln extends ListenerAdapter implements CommandTemplate
{
    private final String COMMAND = Odlaczeni.PREFIX + "pln";
    private final String USAGE = Odlaczeni.PREFIX + "pln` LUB `"+Odlaczeni.PREFIX + "pln @uzytkownik`";
    private final String DESCRIPTION = "pokazuje ilość gotówki w portfelu";

    public CommandPln()
    {
        Commands.addCommand(COMMAND,USAGE,DESCRIPTION,false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        String[] args = event.getMessage().getContentRaw().split(" ");

        if(!args[0].equalsIgnoreCase(COMMAND))
            return;

        User user = event.getAuthor();
        EmbedBuilder eb = new EmbedBuilder();

        if(args.length == 1)
        {
            String description;
            try{
                description = getEmbedDescription(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się pobrać wiadomości użytkownika "+user.getId()+":",throwables);
                return;
            }

            eb.setThumbnail(user.getAvatarUrl());
            eb.setTitle("Portfel użytkownika " + user.getName());
            eb.setDescription(description);
            eb.setColor(0x00ff55);
        }
        else if(args.length == 2)
        {
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
        return false;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> allowedIds() {
        return null;
    }
}

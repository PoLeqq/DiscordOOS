package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBActivity;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandLvl extends ListenerAdapter implements CommandTemplate
{
    private final String COMMAND = Odlaczeni.PREFIX + "lvl";
    private final String USAGE = Odlaczeni.PREFIX + "lvl` LUB `"+Odlaczeni.PREFIX + "lvl @uzytkownik`";
    private final String DESCRIPTION = "pokazuje statystyki aktywności";

    public CommandLvl()
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
                description = getEmbedTitle(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się pobrać wiadomości użytkownika "+user.getId()+":",throwables);
                return;
            }

            eb.setThumbnail(user.getAvatarUrl());
            eb.setTitle("Aktywność");
            eb.setDescription(description);
        }
        else if(args.length == 2)
        {
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
        }

        event.getChannel().sendMessageEmbeds(eb.build()).queue();
    }

    private String getEmbedTitle(User user) throws SQLException
    {
        DBActivity activity = Odlaczeni.getActivity();

        String lvl = "**Lvl:** "+activity.getLvl(user.getId())+"\n";
        String progress = "**Postep:** "+activity.getProgressBar(user.getId())+" "+activity.getProgressPercent(user.getId())+"%\n";
        String exp = "**Exp:** "+activity.getExp(user.getId())+"\n";
        String top = "**Top:** "+activity.getUserPositionByExp(Long.parseLong(user.getId()))+"\n";
        if(activity.getUserPositionByExp(Long.parseLong(user.getId())) == -1)
            top = "**TOP:** `brak danych :(`"+"\n";
        String allExp = "**Exp (łącznie):** "+activity.getAllExp(user.getId())+"\n";

        return top+lvl+exp+progress+allExp;
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

package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBActivity;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.ArgSystem;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommandLvl extends CommandTemplate
{
    public CommandLvl()
    {
        super("lvl",new String[]{},"lvl` LUB `"+Odlaczeni.PREFIX + "lvl @uzytkownik","pokazuje statystyki aktywności",false);
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
                description = getEmbedTitle(user);
            } catch (SQLException e) {
                e.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się pobrać wiadomości użytkownika "+user.getId()+":",e);
                return;
            }

            eb.setThumbnail(user.getAvatarUrl());
            eb.setTitle("Aktywność");
            eb.setDescription(description);
        }
        else if(args.length == 1)
        {
            if(!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE))
            {
                event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                        message.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }

            ArgSystem arg = new ArgSystem();
            User target = arg.getUser(args[0]);

            if(target == null)
            {
                event.getChannel().sendMessage(MessageSystem.Errors.USER_NOT_FOUND).queue();
                return;
            }

            String description;
            try{
                description = getEmbedTitle(target);
            } catch (SQLException e) {
                e.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się pobrać wiadomości użytkownika "+target.getId()+":",e);
                return;
            }

            eb.setThumbnail(target.getAvatarUrl());
            eb.setTitle("Aktywność ("+target.getName()+")");
            eb.setDescription(description);
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
}

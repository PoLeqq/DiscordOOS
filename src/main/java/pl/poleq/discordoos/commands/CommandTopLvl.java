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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CommandTopLvl extends ListenerAdapter implements CommandTemplate
{
    private final String COMMAND = Odlaczeni.PREFIX + "toplvl";
    private final String USAGE = Odlaczeni.PREFIX + "toplvl`";
    private final String DESCRIPTION = "pokazuje top 10 najbardziej aktywnych użytkowników";

    public CommandTopLvl()
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

        DBActivity activity = Odlaczeni.getActivity();
        EmbedBuilder eb = new EmbedBuilder();

        if(args.length == 1)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));

            eb.setThumbnail(Odlaczeni.bot.getSelfUser().getAvatarUrl());
            eb.setColor(0x00fffaa);
            eb.setTitle("Topka aktywności:");
            StringBuilder description = new StringBuilder();
            for(int i=1; i<=10; i++)
            {
                User topUser;
                try {
                    topUser = activity.getUser(i);
                } catch (SQLException e) {
                    e.printStackTrace();
                    CommandErrorsChannel.logToChannel("Nie udało się pobrać użytkownika ",e);
                    return;
                }
                if(topUser == null)
                    continue;

                try {
                    description.append("**").append(i).append(".** ").append(topUser.getName()).append(" \u00AD \u00AD \u00AD(").append(activity.getAllExp(topUser.getId())).append(")\n");
                } catch (SQLException e) {
                    e.printStackTrace();
                    CommandErrorsChannel.logToChannel("Nie udało się pobrać informacji użytkownika ",e);
                    return;
                }
            }
            eb.setDescription(description.toString());
            eb.setFooter("Dane z: "+sdf.format(new Date()));
        }
        else if(args.length == 2)
        {
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        event.getChannel().sendMessageEmbeds(eb.build()).queue();
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

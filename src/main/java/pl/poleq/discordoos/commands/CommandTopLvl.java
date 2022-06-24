package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.database.DBActivity;
import pl.poleq.discordoos.logic.CommandTemplate;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CommandTopLvl extends CommandTemplate
{
    public CommandTopLvl()
    {
        super("toplvl",new String[]{"tlvl"},"toplvl","pokazuje topkę 10 najbardziej aktywnych użytkowników",false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isCommand(event))
            return;

        String[] args = getArgs(event.getMessage().getContentRaw());

        DBActivity activity = Odlaczeni.getActivity();
        EmbedBuilder eb = new EmbedBuilder();

        if(args.length == 0)
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
        else if(args.length == 1)
        {
            //TODO
            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
                    message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }

        event.getChannel().sendMessageEmbeds(eb.build()).queue();
    }
}

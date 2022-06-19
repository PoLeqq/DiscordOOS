package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.database.DBActivity;
import pl.poleq.discordoos.database.DBActivityRewards;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OnChat extends ListenerAdapter
{
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;

        if(!event.getChannel().getId().equals("973990702159650887"))
            return;

        DBActivity activity = Odlaczeni.getActivity();
        int oldUserLvl;
        int newUserLvl;
        try {
            oldUserLvl = activity.getLvl(event.getAuthor().getId());
            activity.addData(event.getMessage());
            newUserLvl = activity.getLvl(event.getAuthor().getId());
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel(e);
            return;
        }

        if(oldUserLvl != newUserLvl)
        {
            //LVl'upka
            String lvlupMessage = getRandomLvlupMessage();
            lvlupMessage = lvlupMessage.replaceAll("%user%", event.getAuthor().getAsMention());
            lvlupMessage = lvlupMessage.replaceAll("%lvl%", String.valueOf(newUserLvl));
            event.getGuild().getTextChannelById(973990702159650887L).sendMessage(lvlupMessage).queue();

            //Czy należy się jakaś nagroda?
            DBActivityRewards ar = Odlaczeni.getActivityRewards();
            long roleReward = 0;
            try {
                roleReward = ar.getRoleReward(newUserLvl);
            } catch (SQLException e) {
                e.printStackTrace();
                CommandErrorsChannel.logToChannel("Jakiś błąd xD",e);
            }
            if(roleReward == -1)
                return;

            Role role = Odlaczeni.bot.getGuildById(Odlaczeni.SERVERID).getRoleById(roleReward);
            String rrMessage = getRandomRoleRewardMessage();
            rrMessage = rrMessage.replaceAll("%user%",event.getAuthor().getAsMention());
            rrMessage = rrMessage.replaceAll("%role%",role.getName());

            event.getGuild().getTextChannelById(973990702159650887L).sendMessage(rrMessage).queue();
            event.getMember().getRoles().add(role);
        }
    }

    private String getRandomRoleRewardMessage()
    {
        List<String> messages = new ArrayList<>();
        messages.add("Dzięki, że jesteś aktywny, %user%! W ramach podziękowań otrzymałeś role %role%");
        messages.add("GG %user%, otrzymałeś role %role%");
        messages.add("Widzę że bardzo się starasz, %user%! Otrzymujesz %role%!");
        messages.add("Dzięki za rozwój naszego community %user%! W ramach podziękować otrzymujesz %role%");
        messages.add("...A kolejna superowa rola (%role%) należy się.... %user%!");
        messages.add("Hej %user%! Ten lvl posiada specjalną nagrodę, czyli %role%!");

        Random random = new Random();
        int rand = random.nextInt(messages.size());

        return messages.get(rand);
    }

    private String getRandomLvlupMessage()
    {
        List<String> messages = new ArrayList<>();
        messages.add("Psst, %user%! Właśnie awansowałeś na lvl %lvl%!");
        messages.add("GG, %user% awansował na poziom %lvl%!");
        messages.add("Gratulacje %user%, masz już %lvl% poziom!");
        messages.add("Za twoją aktywność na serwerze, nagradzam Ciebie, %user% na %lvl% poziom.");
        messages.add("Trzymaj tak dalej %user%, to już %lvl% poziom!");
        messages.add("Brawo %user%, jesteś już na %lvl% poziomie!");

        Random random = new Random();
        int rand = random.nextInt(messages.size());

        return messages.get(rand);
    }
}

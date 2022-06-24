package pl.poleq.discordoos.system;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;
import pl.poleq.discordoos.Odlaczeni;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgSystem
{
    public User getUser(String arg)
    {
        arg = arg.strip();

        Pattern userPattern;
        Matcher userMatcher;
        if(arg.length() == 21)
        {
            userPattern = Pattern.compile("<@\\d{18}>");
            userMatcher = userPattern.matcher(arg);
            arg = arg.substring(2, arg.length()-1);
        }
        else
        {
            userPattern = Pattern.compile("\\d{18}");
            userMatcher = userPattern.matcher(arg);
        }

        if(!userMatcher.matches())
            return null;

        return Odlaczeni.bot.retrieveUserById(arg).complete();
    }

    public GuildChannel getChannel(String arg)
    {
        arg = arg.strip();

        Pattern userPattern;
        Matcher userMatcher;
        if(arg.length() == 21)
        {
            userPattern = Pattern.compile("<#\\d{18}>");
            userMatcher = userPattern.matcher(arg);
            arg = arg.substring(2, arg.length()-1);
        }
        else
        {
            userPattern = Pattern.compile("\\d{18}");
            userMatcher = userPattern.matcher(arg);
        }

        if(!userMatcher.matches())
            return null;

        return Odlaczeni.bot.getGuildChannelById(arg);
    }

    public long getSeconds(String[] args)
    {
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        for(String s : args)
        {
            if(s.endsWith("d"))
                days = Integer.parseInt(s.substring(0,s.indexOf('d')));
            else if(s.endsWith("h"))
                hours = Integer.parseInt(s.substring(0,s.indexOf('h')));
            else if(s.endsWith("m"))
                minutes = Integer.parseInt(s.substring(0,s.indexOf('m')));
            else if(s.endsWith("s"))
                seconds = Integer.parseInt(s.substring(0,s.indexOf('s')));
        }

        return days*86400L + hours*3600L + minutes*60L + seconds;
    }
}

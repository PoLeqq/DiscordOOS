package pl.poleq.discordoos.events;

import net.dv8tion.jda.api.JDA;

public class Events
{
    public Events(JDA bot)
    {
//        bot.addEventListener(new Partnerships());
        bot.addEventListener(new Applications());
        bot.addEventListener(new OnChat());
        bot.addEventListener(new OnJoin());
        bot.addEventListener(new OnPing());
        bot.addEventListener(new AllEvents());
        bot.addEventListener(new Funfacts());
        new OnEnable(bot);
    }
}

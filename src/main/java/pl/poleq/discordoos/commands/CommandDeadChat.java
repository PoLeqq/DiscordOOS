package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CommandDeadChat extends CommandTemplate
{
    private long lastUsage;

    public CommandDeadChat()
    {
        super("deadchat",new String[]{"dchat","deadc"},"deadchat","pinguje role <@&970983738106327070>",false);
        lastUsage = 0;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isInGuild(event))
            return;
        if(!isCommand(event))
            return;
        if(getArgs(event.getMessage().getContentRaw()).length != 0)
            return;
        if(System.currentTimeMillis() - lastUsage < 36000000)
            return;

        lastUsage = System.currentTimeMillis();

        String message = getRandomMessage();
        message = message.replaceAll("%user%","<@"+event.getAuthor().getId()+">");

        Objects.requireNonNull(event.getGuild().getTextChannelById(973990702159650887L))
                .sendMessage("<@&970983738106327070>\n" +
                        message+"\n" +
                        "Jezeli chcesz wybrać temat rozmowy, wpisz `;temat`").queue();
    }

    private String getRandomMessage()
    {
        List<String> messages = new ArrayList<>();

        messages.add("Czas trochę popisać, co o tym sądzicie? <:PandaThink:977892205891039262>");
        messages.add("Witaj świecie!");
        messages.add("Budzimy się czat!");
        messages.add("Hej, trochę tutaj pusto, wzywamy was!");
        messages.add("%user% poczuł się samotnie <:Panda_Tears:977892205454831677>!");

        Random random = new Random();
        int rand = random.nextInt(messages.size()-1);

        return messages.get(rand);
    }
}

package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CommandDeadChat extends ListenerAdapter implements CommandTemplate
{
    private final String COMMAND = Odlaczeni.PREFIX + "deadchat";
    private final String USAGE = Odlaczeni.PREFIX + "deadchat`";
    private final String DESCRIPTION = "pinguje role <@&970983738106327070>";

    private long lastUsage;

    public CommandDeadChat()
    {
        lastUsage = 0;
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

        if(System.currentTimeMillis() - lastUsage < 36000000)
            return;

        lastUsage = System.currentTimeMillis();

        String message = getRandomMessage();
        message = message.replaceAll("%user%","<@"+event.getAuthor().getId()+">");

        Objects.requireNonNull(event.getGuild().getTextChannelById(973990702159650887L)).sendMessage(message).queue();
        Objects.requireNonNull(event.getGuild().getTextChannelById(973990702159650887L)).sendMessage("<@&970983738106327070>! Jezeli chcesz wybrać temat rozmowy, wpisz `;temat`").queue();
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

package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommandPomoc extends ListenerAdapter implements CommandTemplate
{
    private final String COMMAND = Odlaczeni.PREFIX + "pomoc";
    private final String USAGE = Odlaczeni.PREFIX + "pomoc`";
    private final String DESCRIPTION = "komenda zawierająca wszystkie komendy wraz z opisami";

    public CommandPomoc()
    {
        Commands.addCommand(COMMAND,USAGE,DESCRIPTION,false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if(event.getAuthor().isSystem() || event.getAuthor().isBot())
            return;

        if(args.length != 1)
            return;

        if(!args[0].equalsIgnoreCase(COMMAND))
            return;

        StringBuilder description = new StringBuilder();

        for(int i = 0; i < Commands.getCommands().size(); i++)
        {
            if(Commands.getAdminCommands().contains(Commands.getCommands().get(i)))
                continue;

            description.append("**").append(Commands.getCommands().get(i)).append("** ");
            description.append("(`").append(Commands.getUsages().get(i)).append(") ");
            description.append("- ").append(Commands.getDescriptions().get(i)).append("\n\n");
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(0x00ff00);
        embed.setTitle("POMOC");
        embed.setDescription(description);
        embed.setThumbnail("https://poleq.addhost.pl/img/bot.gif");
        embed.setFooter(sdf.format(date));

        event.getChannel().sendMessageEmbeds(embed.build()).queue();
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

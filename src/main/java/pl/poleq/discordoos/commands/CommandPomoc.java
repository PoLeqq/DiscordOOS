package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CommandPomoc extends CommandTemplate
{
    public CommandPomoc()
    {
        super("pomoc",null,"pomoc","komenda zawierająca wszystkie komendy wraz z opisami",false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isCommand(event))
            return;

        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args.length != 1)
            return;

        StringBuilder description = new StringBuilder();

        // Dodanie komendy AdminPomoc
        {
            CommandTemplate ap = Commands.getAdminCommands().get(0);
            description.append("**").append(ap.getCommand()).append("** ");
            description.append("(`").append(ap.getUsage()).append("`) ");
            description.append("- ").append(ap.getDescription()).append("\n\n");
            description.append("Aliasy: `");
            if(ap.getAliases() == null)
                description.append("[]");
            else
                description.append(Arrays.toString(ap.getAliases()));
            description.append("`\n\n");
        }

        for(int i = 0; i < Commands.getCommands().size(); i++)
        {
            if(Commands.getAdminCommands().contains(Commands.getCommands().get(i)))
                continue;

            CommandTemplate command = Commands.getCommands().get(i);
            description.append("**").append(command.getCommand()).append("** ");
            description.append("(`").append(command.getUsage()).append("`) ");
            description.append("- ").append(command.getDescription()).append("\n");
            description.append("Aliasy: `");
            if(command.getAliases() == null)
                description.append("[]");
            else
                description.append(Arrays.toString(command.getAliases()));
            description.append("`\n\n");
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
}

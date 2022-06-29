package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class CommandAdminPomoc extends CommandTemplate
{
    public CommandAdminPomoc()
    {
        super("apomoc", null, "apomoc", "komenda zawierająca wszystkie komendy dla admnistratorów wraz z opisami", true);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isInGuild(event))
            return;
        if(!isCommand(event))
            return;
        if(!canMemberUse(Objects.requireNonNull(event.getMember()),null))
            return;

        String[] args = getArgs(event.getMessage().getContentRaw());
        if(args.length != 1)
            return;

        Member member = event.getGuild().retrieveMember(event.getAuthor()).complete();
        if(!Objects.requireNonNull(member).hasPermission(Permission.ADMINISTRATOR))
            return;

        StringBuilder description = new StringBuilder();

        for(int i = 0; i < Commands.getCommands().size(); i++)
        {
            if(!Commands.getAdminCommands().contains(Commands.getCommands().get(i)))
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

        embed.setColor(0xff0000);
        embed.setTitle("POMOC");
        embed.setDescription(description);
        embed.setThumbnail("https://poleq.addhost.pl/img/bot.gif");
        embed.setFooter(sdf.format(date));

        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}

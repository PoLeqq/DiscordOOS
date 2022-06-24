package pl.poleq.discordoos.commands;

import org.jetbrains.annotations.Nullable;
import pl.poleq.discordoos.logic.CommandTemplate;

public class CommandPartnershipChannel extends CommandTemplate //implements CommandTemplate
{
    public CommandPartnershipChannel(String command, @Nullable String[] aliases, String usage, String description, boolean adminCommand) {
        super(command, aliases, usage, description, adminCommand);
    }
//    private final String COMMAND = Odlaczeni.PREFIX + "pschannel";
//    private final String USAGE = Odlaczeni.PREFIX + "pschannel <#kanał>`";
//    private final String DESCRIPTION = "ustawia kanał, na który jest odpowiedzialny za kanał do partnerstw międzyserwerowych";
//
//    public CommandPartnershipChannel()
//    {
//        Commands.addCommand(COMMAND,USAGE,DESCRIPTION,false);
//    }
//
//    @Override
//    public void onMessageReceived(@NotNull MessageReceivedEvent event)
//    {
//        if(event.getAuthor().isBot() || event.getAuthor().isSystem())
//            return;
//
//        String[] args = event.getMessage().getContentRaw().split(" ");
//
//        if(!args[0].equalsIgnoreCase(COMMAND))
//            return;
//
//        if(!event.getGuild().getMemberById(event.getAuthor().getId()).hasPermission(Permission.ADMINISTRATOR))
//        {
//            event.getChannel().sendMessage(MessageSystem.Errors.NO_PERMISSION).queue((message) ->
//                    message.delete().queueAfter(5, TimeUnit.SECONDS));
//            return;
//        }
//
//        MessageChannel mc = event.getChannel();
//        if(args.length != 2)
//        {
//            mc.sendMessage(MessageSystem.Errors.USAGE + USAGE).queue();
//            return;
//        }
//
//        // Jeżeli drugi argument to "reset", zresetuj kanał
//        if(args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("restart") || args[1].equalsIgnoreCase("null"))
//        {
//            Partnerships.setSayChannel(null);
//            mc.sendMessage("Zresetowano kanał wysyłania wiadomości!").queue();
//            return;
//        }
//
//        // Sprawdzenie drugiego argumentu, czy jest on faktycznie poprawny
//        Pattern channelPattern = Pattern.compile("<#\\d{18}>");
//        Matcher channelMatcher = channelPattern.matcher(args[1]);
//
//        if(!channelMatcher.matches())
//        {
//            mc.sendMessage(MessageSystem.Errors.NOT_A_CHANNEL).queue();
//            return;
//        }
//
//        String channelId = args[1].substring(2, args[1].length()-1);
//        MessageChannel targetChannel = (MessageChannel) event.getGuild().getGuildChannelById(channelId);
//        if(targetChannel == null)
//        {
//            mc.sendMessage(MessageSystem.Errors.CHANNEL_NOT_FOUND).queue();
//            return;
//        }
//
//        Partnerships.setSayChannel(targetChannel);
//        mc.sendMessage("Zmieniono kanal na <#" + targetChannel.getId()+">!").queue();
//    }
}

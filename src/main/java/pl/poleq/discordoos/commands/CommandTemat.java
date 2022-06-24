package pl.poleq.discordoos.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.logic.CommandTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CommandTemat extends CommandTemplate
{
    public CommandTemat()
    {
        super("temat",new String[]{},"temat","losuje temat do rozmów",false);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        if(!isInGuild(event))
            return;
        if(!isCommand(event))
            return;

        String[] args = getArgs(event.getMessage().getContentRaw());
        if(args.length != 0)
            return;

        String message = getRandomMessage();
        Objects.requireNonNull(event.getGuild().getTextChannelById(973990702159650887L)).sendMessage(message).queue();
    }

    private String getRandomMessage()
    {
        List<String> messages = new ArrayList<>();

        messages.add("Jaki jest Twój ulubiony filmy i/lub serial?");
        messages.add("Ostatnio przeczytana książka (o ile jakaś była? xD) to:");
        messages.add("Twój ulubiony utwór?");
        messages.add("Jaka jest Twojaulubiona gra komputerowa?");
        messages.add("Opowiedz dziwną historię z Twojego życia");
        messages.add("Najzabawniejszy przypał gdzieś xD");
        messages.add("Opowiedz trochę o twoim hobby");
        messages.add("Jak ci minął dzionek?");
        messages.add("Jakie masz plany na wakacje?");
        messages.add("Jakie masz plany na najbliższy weekend?");
        messages.add("Jak tam w szkole?");
        messages.add("Opisz swoje marzenia na przyszłość");
        messages.add("Psy czy koty? Uzadadnij :)");
        messages.add("Ulubione zwierzę?");
        messages.add("Przekąski słone, czy słodkie – które lepsze?");
        messages.add("Najlepszy smak lodów to:");
        messages.add("Skąd jesteś? (jeżeli bardzo się wstydzisz, wystarczy województwo, ||my tu nie zbieramy danych i tak :p||)");
        messages.add("Masz rodzeństwo?");
        messages.add("Ulubiony przedmiot szkolny?");
        messages.add("Masz jakiś wymarzony zawód, czy przyszłośc jeszcze Ci nieznana?");
        messages.add("Uprawiasz jakiś sport? Jeżeli tak, to chodzisz do jakiś klubów itp.?");
        messages.add("Kraj, do który chcesz odwiedzić w przyszłości to:    .. i dlaczego?");
        messages.add("Co sądzisz o niedalekiej przyszłości?");
        messages.add("Auto marzeń");
        messages.add("Twoje największe marzenie? Jeżeli masz kilka, możesz śmiało wymienić je wszystkie!");
        messages.add("Udało Ci się kiedyś coś wygrać? Kiedy to było i jak to się właściwie stało?");
        messages.add("Jesteś bardziej typem domownika, czy wolisz zwiedzać świat?");
        messages.add("Najgłupszy sposób na zarobienie jak najwięcej w 1 dzień?");
        messages.add("Co byś zrobił(a), jeżeli byś wygrał(a) milion złotych?");
        messages.add("Potrafisz gotować? Jeżeli tak, co najlepiej ci wychodzi?");
        messages.add("Co sądzisz o aktualnym świecie? Idzie w dobrą, czy złą stronę?");
        messages.add("Czy według ciebie postęp technologiczny jest faktycznie zagrożeniem dla nas w przyszłosci?");
        messages.add("Jaką supermoc chciał(a)być mieć (np. latanie, czytanie w myślach) i czemu?");
        messages.add("Najlepsza pora roku to:");
        messages.add("Najlepsza pora dnia to:");
        messages.add("Twoja najwięsza pasja:");
        messages.add("Masz jakiegoś idola?");
        messages.add("Co jest twoją największą inspiracją w życiu?");
        messages.add("Znasz jakiś śmieszny żart? Opowiedz");
        messages.add("Ulubiona część garderoby:");
        messages.add("Wolisz siedzieć w domu czy wyjść gdzieś z przyjaciółmi?");
        messages.add("Twoja największa wada?");
        messages.add("Twoja największa zaleta?");

        Random random = new Random();
        int rand = random.nextInt(messages.size());

        return messages.get(rand);
    }
}

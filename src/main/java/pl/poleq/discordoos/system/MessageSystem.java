package pl.poleq.discordoos.system;

public abstract class MessageSystem
{
    public static class Messages
    {
        public static final String SHUT_DOWN = "Bot za chwilę zostanie wyłączony!";
        public static final String STOP_SHUT_DOWN = "Zamknieto sekwencję wyłączania bota";
    }
    public static class Errors
    {
        public static final String USER_NOT_FOUND = "Nie znaleziono użytkownika!";
        public static final String CHANNEL_NOT_FOUND = "Nie odnaleziono kanału!";
        public static final String NO_PERMISSION = "Nie masz uprawnień!";
        public static final String USAGE = "Niepoprawne użycie! Spróbuj: `";
    }
}

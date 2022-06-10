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
        public static final String NOT_A_CHANNEL = "To nie kanał!";
        public static final String CHANNEL_NOT_FOUND = "Nie odnaleziono kanału!";
        public static final String NO_PERMISSION = "Nie masz uprawnień!";
        public static final String USAGE = "Niepoprawne użycie! Spróbuj: `";
        public static final String DB_ERROR = "Błąd bazy danych: ";
        public static final String WRONG_SQL = "Błędne zapytanie SQL";
        public static final String SQL_ERROR = "Błąd podczas wykonywania zapytania SQL";
        public static final String GET_SQL_ERROR = "Błąd podczas pobierania danych z zapytania SQL";
        public static final String STMT_CREATE_ERROR = "Nie udało się utworzyć statement'u";
    }
}

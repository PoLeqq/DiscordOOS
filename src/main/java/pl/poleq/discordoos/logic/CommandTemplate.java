package pl.poleq.discordoos.logic;

import java.util.List;

public interface CommandTemplate
{
    /**
     * Funkcja powinna sprawdzac, czy argumenty zgadzaja sie z wymaganymi. Jezeli nie, zwroci wartosc false.
     * @param args argumenty komendy
     * @return poprawnosc argumentow
     */
    boolean usage(String[] args);

    /**
     * Funkcja powinna sprawczac, czy wartosc argumentow zgadzaja sie z poprawnymi. Jezeli nie, zwroci wartosc false.
     * @param args argumenty komendy
     * @return poprawnosc wartosci argumentow
     */
    boolean args(String[] args);

    /**
     * Funkcja powinna sprawdzac, czy uzytkownik ma odpowiednie uprawnienia do wysylania komendy
     * @param id id uzytkownika
     * @param permission nazwa permisji
     * @return czy uzytnownik ma uprawnienia
     */
    boolean perms(String id,String permission);

    /**
     *
     * @return czy to komenda administracyjna
     */
    boolean isAdminCommand();

    /**
     * Funkcja powinna zwracac uzycie komendy
     * @return uzycie komendy
     */
    String getUsage();

    /**
     * Funkcja powinna zwracac komende, do ktorej klasa jest przypisana
     * @return komenda
     */
    String getCommand();

    /**
     * Funkcja powinna zwracac opis komendy
     * @return opis komendy
     */
    String getDescription();

    /**
     * Funkcja powinna zwracac liste ID uzytkownikow, ktorzy moga uzywac komende
     * @return lista uprawnionych uzytkownikow
     */
    List<String> allowedIds();
}

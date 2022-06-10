package pl.poleq.discordoos.database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Klasa operujaca na tabeli `activity`
 * Dotyczy ona informacji o aktywnosci uzytkownikow
 */
public class DBActivity
{
    private final String TABLE = "activity";
    // Wartość dodawana za każdą wysłaną wiadomością
    private final int messageExp = 5;
    private final Connection connection;

    /**
     * Konstruktor tworzacy tabele, na ktorej nastepnie bedzie mozna operowac, oraz umozliwia dokonywanie polaczen z baza
     */
    public DBActivity()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`user` VARCHAR(18), `messages` INT, `exp` INT, `all_chars` BIGINT"))
                LogSystem.defaultLog("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    /**
     * Zwraca ilosc wiadomosci wyslanych przez uzytkownika
     * @param userID ID uzytkownika
     * @return wszystkie wyslane wiadomosci
     */
    public int getMessages(String userID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `messages` FROM "+TABLE+" WHERE `user`='"+userID+"'");

        int messages;
        if(rs.next())
            messages = rs.getInt(1);
        else
            messages = 0;

        GeneralDatabase.closeStatement(stmt,"DBActivity.getMessages(String userID)");
        return messages;
    }

    /**
     * Zwraca poziom uzytkownika
     * @param userID ID użytkownika
     * @return poziom użytkownika
     */
    public int getLvl(String userID) throws SQLException
    {
        int exp = getAllExp(userID);
        int lvl = 0;

        while(true)
        {
            if(exp - getRequiredExpToLvlup(lvl-1) < getRequiredExpToLvlup(lvl))
            {
                return lvl;
            }
            lvl++;
        }
    }

    /**
     * Funkcja zwraca wartosc EXP'a wymaganego do osiagniecia danego lvlu
     * @param lvl lvl
     * @return exp wymagany do lvlup'a
     */
    public int getRequiredExpToLvlup(int lvl)
    {
        return lvl*500;
    }

    /**
     * Funkcja zwracajaca całkowity EXP uzytkownika
     * @param userID ID uzytkownika
     * @return EXP uzytkownika
     */
    public int getAllExp(String userID) throws SQLException
    {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT `exp` FROM "+TABLE+" WHERE `user`='"+userID+"'");

        int exp;
        if(rs.next())
            exp = rs.getInt(1);
        else
            exp = 0;

        GeneralDatabase.closeStatement(stmt,"DBActivity.getAllExp(String userID)");
        return exp;
    }

    /**
     * Funkcja zwracajaca EXP uzytkownika (uwzględniając lvl)
     * @param userID ID uzytkownika
     * @return EXP uzytkownika
     */
    public int getExp(String userID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `exp` FROM "+TABLE+" WHERE `user`='"+userID+"'");

        int exp;
        if(rs.next())
            exp = rs.getInt(1);
        else
            exp = 0;

        int lvlExp = getRequiredExpToLvlup(getLvl(userID)-1);
        exp -= lvlExp;

        GeneralDatabase.closeStatement(stmt,"DBActivity.getExp(String userID)");
        return exp;
    }

    /**
     * Funkcja zwracajaca ilosc znakow napisanych przez uzytkownika
     * @param userID ID uzytkownika
     * @return ilosc znakow napisanych przez uzytkownika
     */
    public int getAllChars(String userID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `all_chars` FROM "+TABLE+" WHERE `user`='"+userID+"'");

        int allChars;
        if(rs.next())
            allChars = rs.getInt(1);
        else
            allChars = 0;

        GeneralDatabase.closeStatement(stmt,"DBActivity.getAllChars(String userID)");
        return allChars;
    }

    /**
     * Funkcja dodajaca uzytkownika do bazy danych (jezeli wczesniej w niej nie istanial) lub
     * zmieniajaca wartosci przypisane do konta. Funkcja zwraca:<br>
     * <b>False</b> - kiedy nie dodano/zmieniono wartosci (wystapil blad)<br>
     * <b>True</b> - kiedy dodano/zmieniono wartosci (brak jakiegokolwiek bledu)
     * @param message wiadomosc z eventu
     * @return czy dodano
     */
    public boolean addData(Message message) throws SQLException
    {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM "+TABLE+" WHERE `user`='"+message.getAuthor().getId()+"'");

        if(rs.next())
            updateUser(stmt,message);
        else
            addUser(stmt,message);

        GeneralDatabase.closeStatement(stmt,"DBActivity.addData(Message message)");
        return true;
    }

    /**
     * Funkcja dodajaca nowy rekord do bazy danych z uzytkownikiem
     * @param stmt statement
     * @param message wiadomosc z eventu
     * @return czy dodano
     */
    private boolean addUser(@NotNull Statement stmt, @NotNull Message message) throws SQLException
    {
        int chars = message.getContentStripped().length();
        int exp = getExp(message.getAuthor(),chars) + messageExp;

        stmt.executeUpdate("INSERT INTO "+TABLE+" (`user`,`messages`,`exp`,`all_chars`) VALUES ('"+message.getAuthor().getId()+"', '1', '"+exp+"', '"+message.getContentStripped().length()+"')");

        GeneralDatabase.closeStatement(stmt,"DBActivity.addUser(@NotNull Statement stmt, @NotNull Message message)");
        return true;
    }

    /**
     * Funkcja zmieniajaca wartosci przypisanemu uzytkownikowi w bazie
     * @param message wiadomosc z eventu
     * @return czy zmieniono
     */
    private boolean updateUser(@NotNull Statement stmt, @NotNull Message message) throws SQLException
    {
        int messages = getMessages(message.getAuthor().getId())+1;
        int exp = getAllExp(message.getAuthor().getId())+getExp(message.getAuthor(),message.getContentStripped().length()) + messageExp;
        int allChars = getAllChars(message.getAuthor().getId())+getExp(message.getAuthor(),message.getContentStripped().length());

        stmt.executeUpdate("UPDATE "+TABLE+" SET `messages` = "+messages+", `exp` = '"+exp+"', `all_chars` = '"+allChars+"' WHERE `user`='"+message.getAuthor().getId()+"'");

        GeneralDatabase.closeStatement(stmt,"DBActivity.updateUser(@NotNull Statement stmt, @NotNull Message message)");
        return true;
    }

    /**
     * Podaje ilosc expa nalezaca sie uzytkownikowi za wiadomosc, uwzglednia boosty.
     * @param chars znaki w wiadomosci (getContentStripped)
     * @return ilosc expa (wszystkie znaki * boost)
     */
    @SuppressWarnings("ConstantConditions")
    private int getExp(User user, int chars)
    {
        Member member = Odlaczeni.bot.getGuildById(958640705507573770L).getMember(user);
        float boost = 1;

        if(member != null)
        {
            List<Role> userRoles = member.getRoles();

            DBActivityBoosts ab = Odlaczeni.getActivityBoosts();
            float rankBoost;
            for (Role r : userRoles)
            {
                try {
                    rankBoost = ab.getBoost(Long.parseLong(r.getId()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    CommandErrorsChannel.logToChannel("Nie udało się pobrać danych roli",e);
                    return -1;
                }
                if (rankBoost < 0)
                    continue;
                boost += rankBoost;
            }
        }

        return (int) (chars * boost);
    }

    /**
     * Funkcja zwracajaca postep w lvl'u jako pasek postepu
     * @param userID ID uzytkownika
     * @return pasek postepu
     */
    public String getProgressBar(String userID) throws SQLException
    {
        int percent = getProgressPercent(userID);

        StringBuilder bar = new StringBuilder("[");

        for(int i=0; i<10; i++)
        {
            if(percent > i*10)
                bar.append('■');
            else
                bar.append('□');
        }
        bar.append(']');

        return bar.toString();
    }

    /**
     * Funkcja zwracajaca postep w lvl'u jako procent
     * @param userID ID uzytkownika
     * @return procent postepu
     */
    public int getProgressPercent(String userID) throws SQLException
    {
        int userExp = getExp(userID);
        int lvlExp = getRequiredExpToLvlup(getLvl(userID));

        return (int) Math.round((userExp * 100.0) / (lvlExp * 100.0) * 100.0);
    }

    /**
     * Funkcja zwracajaca range uzytkownika (topka)
     * @return uzytkownika gracza w topce
     */
    public int getUserPositionByExp(long userID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM "+TABLE+" ORDER BY `exp` DESC");

        int i=1;
        while(rs.next())
        {
            long dbID = rs.getLong("user");
            if(userID == dbID)
                return i;
            i++;
        }
        GeneralDatabase.closeStatement(stmt,"DBActivity.getUserPositionByExp(long userID)");
        return -1;
    }

    /**
     * Funkcja zwracajaca ozytkownika poprzez jego miejsce w topce
     * @param top miejsce w topce
     * @return uzytkownik
     */
    public User getUser(int top) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT * FROM "+TABLE+" ORDER BY `exp` DESC LIMIT 1 OFFSET "+(top-1));

        if(!rs.next())
            return null;

        long userID = rs.getLong("user");

        GeneralDatabase.closeStatement(stmt,"DBActivity.getUserPositionByExp(long userID)");
        RestAction<User> ra = Odlaczeni.bot.retrieveUserById(userID);
        return ra.complete();
    }
}

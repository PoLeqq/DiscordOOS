package pl.poleq.discordoos.database;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBStreamers
{
    private final String TABLE = "streamers";
    // Wartość dodawana za każdą wysłaną wiadomością
    private final int messageExp = 5;
    private final Connection connection;

    /**
     * Konstruktor tworzacy tabele, na ktorej nastepnie bedzie mozna operowac, oraz umozliwia dokonywanie polaczen z baza
     */
    public DBStreamers()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`user` VARCHAR(18), `tw_nick` VARCHAR(40), `tw_link` VARCHAR(80)"))
                LogSystem.defaultLog("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    public List<User> getStreamers() throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `user` FROM "+TABLE);
        List<User> streamers = new ArrayList<>();

        for(int i=0; rs.next(); i++)
        {
            RestAction<User> user = Odlaczeni.bot.retrieveUserById(rs.getLong(i));
            streamers.add(user.complete());
        }

        return streamers;
    }

    public boolean isStreaming(String userID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `tw_nick` WHERE `user` = '"+userID+"'");
        if(!rs.next())
            return false;
        RestAction<User> user = Odlaczeni.bot.retrieveUserById(rs.getLong(1));

        //TU TWITCH API

        return true;
    }
}

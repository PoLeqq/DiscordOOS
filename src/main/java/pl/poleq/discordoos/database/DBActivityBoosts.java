package pl.poleq.discordoos.database;

import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasa operujaca na tabeli `activity_boosts`
 * Dotyczy rang/uzytkownikow z boostem xp, dodatnim badz zerowym
 */
public class DBActivityBoosts
{
    private final String TABLE = "activity_boosts";
    private final Connection connection;

    public DBActivityBoosts()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`type` VARCHAR(4), `dcID` VARCHAR(18), `boost` FLOAT, `end_date` BIGINT"))
                LogSystem.defaultLog("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    public String getType(long ID) throws SQLException {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `type` FROM "+TABLE+" WHERE `dcID`='"+ID+"'");
        if(!rs.next())
            return null;
        String type = rs.getString(1);

        GeneralDatabase.closeStatement(stmt,"DBActivity.getType(long ID)");
        return type;
    }

    public float getBoost(long ID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `boost` FROM "+TABLE+" WHERE `dcID`='"+ID+"'");

        float boost;
        if(rs.next())
            boost = rs.getFloat(1);
        else
        {
            GeneralDatabase.closeStatement(stmt,"DBActivityBoosts.getBoost(long ID)");
            return -1;
        }

        GeneralDatabase.closeStatement(stmt,"DBActivityBoosts.getBoost(long ID)");
        return boost;
    }

    /**
     * Zwraca "koncowe dzialanie" boosta roli badz uzytkownika<br>
     * Funkcja zwraca -1 jezeli nie uda sie znalezc roli lub uzytkownika
     * @param ID ID
     * @return data w milisekundach
     */
    public long getEndDate(long ID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `end_date` FROM "+TABLE+" WHERE `dcID`='"+ID+"'");
        if(!rs.next())
            return -1;

        long date = rs.getLong(1);

        GeneralDatabase.closeStatement(stmt,"DBActivity.getUserPositionByExp(long userID)");
        return date;
    }
}
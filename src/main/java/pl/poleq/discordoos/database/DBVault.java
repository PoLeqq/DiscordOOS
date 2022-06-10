package pl.poleq.discordoos.database;

import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class DBVault
{
    private final Connection connection;
    private final String TABLE = "vault";

    public DBVault()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`user` VARCHAR(18), `pln` FLOAT"))
                LogSystem.defaultLog("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    /**
     * Funkcja zwracajaca ilosc PLN gracza
     * @param userID ID uzytkownika
     * @return pieniadze uzytkownika
     * @throws SQLException e
     */
    public float getPln(String userID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `pln` FROM "+TABLE+" WHERE `user`='"+userID+"'");

        float pln;
        if(rs.next())
            pln = rs.getLong(1);
        else {
            GeneralDatabase.closeStatement(stmt,"DBActivity.getUserPositionByExp(long userID)");
            return 0;
        }

        GeneralDatabase.closeStatement(stmt,"DBActivity.getUserPositionByExp(long userID)");
        return pln;
    }

    public String getDisplayPln(String userID)
    {
        String moneyAsString;
        try {
            moneyAsString = String.valueOf(getPln(userID));
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Błąd podczas pobierania pieniędzy gracza <@"+userID+">",e);
            return null;
        }

        int occurrenceOfDot = moneyAsString.indexOf('.');
        String moneyAsStringAfterDot = moneyAsString.substring(occurrenceOfDot+1);

        if(moneyAsStringAfterDot.length() == 1)
            if(moneyAsStringAfterDot.equals("0"))
                return moneyAsString.substring(0,occurrenceOfDot);
            else
                return moneyAsString.substring(0,occurrenceOfDot+2)+"0";

        return moneyAsString.substring(0,occurrenceOfDot+3);
    }
}
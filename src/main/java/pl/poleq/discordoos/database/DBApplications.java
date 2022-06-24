package pl.poleq.discordoos.database;

import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.*;

public class DBApplications
{
    private final Connection connection;
    private final String TABLE = "applications";

    public DBApplications()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`user` VARCHAR(18), `date` BIGINT, `message` TEXT"))
                LogSystem.defaultLog("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    public long getLastApplication(String userID) throws SQLException
    {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT `date` FROM "+TABLE+" WHERE user='"+userID+"'");

        if(!rs.next())
            return 0;

        GeneralDatabase.closeStatement(stmt,"DBActivity.getUserPositionByExp(long userID)");
        return rs.getLong(1);
    }

    public void insertApplication(String userID, String message) throws SQLException
    {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO "+TABLE+" (`user`,`date`,`message`) VALUES ('"+userID+"', '"+System.currentTimeMillis()+"', '"+message+"')");
        GeneralDatabase.closeStatement(stmt,"DBActivity.getUserPositionByExp(long userID)");
    }
}

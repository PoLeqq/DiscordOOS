package pl.poleq.discordoos.database;

import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.*;

public class DBChannels
{
    private final Connection connection;
    private final String TABLE = "channels";
    public final String PARTNERSHIPS = "partnerships";
    public final String APPLICATIONS = "applications";
    public final String CHANNELS = "channels";
    public final String SAY = "say";

    public DBChannels()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`name` VARCHAR(40), `channelID` VARCHAR(18)"))
                LogSystem.defaultLog("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    public long getChannelID(String channelName) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT channelID FROM "+TABLE+" WHERE name='"+channelName+"'");

        long channelID;
        if(rs.next())
            channelID = rs.getLong(1);
        else {
            CommandErrorsChannel.logToChannel("Nie znaleziono kanału "+channelName);
            LogSystem.defaultLog("Nie znaleziono kanału "+channelName);
            GeneralDatabase.closeStatement(stmt,"DBChannel.getChannelID(String channelName)");
            return 0;
        }

        GeneralDatabase.closeStatement(stmt,"DBChannel.getChannelID(String channelName)");
        return channelID;
    }

    public boolean setChannel(String channelName,long channelID) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT id FROM "+TABLE+" WHERE `name`='"+channelName+"'");

        try {
            if(rs.next())
                updateChannel(stmt,channelName,channelID);
            else
                addChannel(stmt,channelName,channelID);
        } catch (SQLException e) {
            e.printStackTrace();
            GeneralDatabase.closeStatement(stmt,"DBChannel.setChannel(String channelName,long channelID)");
            return false;
        }

        GeneralDatabase.closeStatement(stmt,"DBChannel.setChannel(String channelName,long channelID)");
        return true;
    }

    private boolean addChannel(Statement stmt, String channelName,long channelID)
    {
        try {
            stmt.executeUpdate("INSERT INTO "+TABLE+" (`name`,`channelID`) VALUES ('"+channelName+"', '"+channelID+"')");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Wystąpił błąd:",e);
            GeneralDatabase.closeStatement(stmt,"DBChannel.addChannel(Statement stmt, String channelName,long channelID)");
            return false;
        }
        GeneralDatabase.closeStatement(stmt,"DBChannel.addChannel(Statement stmt, String channelName,long channelID)");
        return true;
    }

    private boolean updateChannel(Statement stmt, String channelName,long channelID)
    {
        try {
            stmt.executeUpdate("UPDATE "+TABLE+" SET `channelID` = "+channelID+" WHERE `name`='"+channelName+"'");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Wystąpił błąd:",e);
            GeneralDatabase.closeStatement(stmt,"DBChannels.updateChannel(Statement stmt, String channelName,long channelID)");
            return false;
        }
        GeneralDatabase.closeStatement(stmt,"DBChannels.updateChannel(Statement stmt, String channelName,long channelID)");
        return true;
    }
}

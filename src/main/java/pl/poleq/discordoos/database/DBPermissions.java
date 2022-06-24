package pl.poleq.discordoos.database;

import net.dv8tion.jda.api.entities.Role;
import pl.poleq.discordoos.Odlaczeni;
import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.logic.Permissions;

import java.sql.*;
import java.util.List;

public class DBPermissions
{
    private final Connection connection;
    private final String TABLE = "permissions";

    public DBPermissions()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`name` VARCHAR(40), `dcID` VARCHAR(18), `idType` VARCHAR(4), `value` BOOLEAN"))
                System.out.println("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    /**
     *
     * @param id id uzytkownika
     * @param idType typ ID (<b>role</b> - rola, <b>user</b> - użytkownik)
     * @param permission typ permisji
     * @param value wartosc ustawianej permisji
     */
    public void setPermission(String id, String idType, Permissions permission, boolean value) throws SQLException
    {
        Statement stmt = connection.createStatement();
        int val = value ? 1 : 0;

        ResultSet rs = stmt.executeQuery("SELECT id FROM "+TABLE+" WHERE dcID='"+id+"' AND `name`='"+permission.getName()+"'");
        if(rs.next())
            stmt.executeUpdate("UPDATE "+TABLE+" SET `value`="+val+" WHERE `name`='"+permission.getName()+"' AND `dcID`='"+id+"'");
        else
            stmt.executeUpdate("INSERT INTO "+TABLE+" (`name`,`dcID`,`idType`,`value`) VALUES ('"+permission.getName()+"','"+id+"','"+idType+"','"+val+"')");

        GeneralDatabase.closeStatement(stmt,"DBChannels.updateChannel(Statement stmt, String channelName,long channelID)");
    }

    public boolean hasPermission(String id, Permissions permission) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `value` FROM "+TABLE+" WHERE `dcID`='"+id+"' AND `name`='"+permission.getName()+"'");
        boolean value = false;
        if(rs.next())
            value = rs.getBoolean(1);

        GeneralDatabase.closeStatement(stmt,"DBChannels.updateChannel(Statement stmt, String channelName,long channelID)");
        return value;
    }

    /**
     * Funcja zwracajaca wartosc true, jezeli chociaz jedna z rol ma uprawnienia.<br>
     * Jezeli zadna z rol nie ma uprawnien, funkcja zwroci wartosc false
     * @param roles lista roli (np. uzytkownika)
     * @return czy uprawnione
     */
    public boolean hasPermission(List<Role> roles, Permissions permission) throws SQLException
    {
        for(Role r : roles)
            if(Odlaczeni.getPermissions().hasPermission(r.getId(), permission))
                return true;

        return false;
    }
}

package pl.poleq.discordoos.database;

import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBActivityRewards
{
    private final Connection connection;
    private final String TABLE = "activity_rewards";

    public DBActivityRewards()
    {
        GeneralDatabase database = new GeneralDatabase();
        connection = database.getConnection();
        try {
            if(database.createTable(TABLE,"`roleID` VARCHAR(18), `lvl` INT"))
                LogSystem.defaultLog("Utworzono tabelę `"+TABLE+"`!");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie udało się utworzyć tabeli "+TABLE,e);
        }
    }

    /**
     * Funkcja zwracajaca ID roli przypisanywanej graczowi po osiegnieciu danego poziomu
     * <br>
     * Zwraca wartość <b>-1</b> kiedy nie ma nagrody
     * @param lvl poziom
     * @return ID roli
     */
    public long getRoleReward(int lvl) throws SQLException
    {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `roleID` FROM "+TABLE+" WHERE `lvl`='"+lvl+"'");

        long roleID;
        if(rs.next())
            roleID = rs.getLong(1);
        else
            roleID = -1;

        GeneralDatabase.closeStatement(stmt,"DBActivityRewards.getRoleReward(int lvl)");
        return roleID;
    }

    /**
     * Funkcja zwracajaca wymagany poziom, ktory musi osiagnac uzytkownik aby dostac dana role
     * <br>
     * Zwraca wartość <b>-1</b> kiedy nie ma takiej roli w nagrodach
     * @param roleID ID roli
     * @return wymagany poziom
     */
    public int getLvlReward(long roleID) throws SQLException {
        Statement stmt = connection.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT `lvl` FROM "+TABLE+" WHERE `roleID`='"+roleID+"'");

        int lvl;

        if(rs.next())
            lvl = rs.getInt(1);
        else
            lvl = -1;

        GeneralDatabase.closeStatement(stmt,"DBActivityRewards.getLvlReward(long roleID)");
        return lvl;
    }
}
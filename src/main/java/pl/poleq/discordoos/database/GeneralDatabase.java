package pl.poleq.discordoos.database;

import pl.poleq.discordoos.commands.CommandErrorsChannel;
import pl.poleq.discordoos.system.LogSystem;
import pl.poleq.discordoos.system.MessageSystem;

import java.sql.*;

class GeneralDatabase
{
    private Connection connection;

    public GeneralDatabase()
    {
        createConnection();
    }

    private void createConnection()
    {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://161.97.78.70:3306/s5464_discordoos?autoReconnect=true", "u5464_woXVI7J3Lq", "00DH+D+g+Hj6FOj0@0Rt!in7");
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Nie można stworzyć połączenia z bazą danych: ",e);
        }
    }

    private void closeConnection()
    {
        if(connection != null)
        {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                CommandErrorsChannel.logToChannel("Nie udało się zamknąć połączenia z bazą",e);
            }
            connection = null;
        }
    }

    public boolean createTable(String name,String sql) throws SQLException
    {
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null,null,name,null);

        if(rs.next())
        {
            LogSystem.defaultLog("Tabela "+name+ " już istnieje, nie utworzono nowej");
            return false;
        }

        Statement stmt = connection.createStatement();
        if(stmt.execute("create table `"+name+"` (`id` INT AUTO_INCREMENT PRIMARY KEY,"+sql+");"))
        {
            stmt.close();
            return true;
        }

        stmt.close();
        return false;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean exeuteSql(String sql) throws SQLException
    {
        Statement stmt = connection.createStatement();
        return stmt.execute(sql);
    }

    /**
     * Funkcja zamykajaca statement
     * @param stmt statement
     * @param functionName nazwa klasy i funkcji (NazwaKlasy.nazwaFunkcji())
     */
    public static void closeStatement(Statement stmt,String functionName)
    {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            CommandErrorsChannel.logToChannel("Wystąpił błąd podczas zamykania statement'u (`"+functionName+"`):",e);
        }
    }
}

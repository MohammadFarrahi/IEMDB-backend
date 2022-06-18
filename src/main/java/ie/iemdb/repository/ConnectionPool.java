package ie.iemdb.repository;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionPool {
    private static final BasicDataSource ds = new BasicDataSource();
    private static String dbURL;
    private static String dbUserName;
    private static String dbPassword;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        var dotenv = Dotenv.load();
        dbURL = dotenv.get("DB_URL");
        dbUserName = dotenv.get("DB_USERNAME");
        dbPassword = dotenv.get("DB_PASSWORD");

        ds.setUsername(dbUserName);
        ds.setPassword(dbPassword);
        ds.setUrl(dbURL);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        setEncoding();
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void setEncoding(){
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("ALTER DATABASE iemdb_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
            connection.close();
            statement.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}

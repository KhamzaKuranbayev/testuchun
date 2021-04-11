package module1.lesson_joins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    public static Connection getConnection(String db) {
        String url = "jdbc:postgresql://localhost:5432/" + db;
        String user = "postgres";
        String password = "start123";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return connection;
    }
}

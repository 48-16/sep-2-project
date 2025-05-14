package persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=barbershop";
    private static final String USER = "postgres";
    private static final String PASSWORD = "asdfghjkl";

    private PostgresConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

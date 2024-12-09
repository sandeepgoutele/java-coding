package connection_pool;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private final java.sql.Connection connection;
    static final String DB_URL = "jdbc:mysql://localhost:3306/practise";
    static final String DB_USER = "sandeep";
    static final String DB_PASSWORD = "sandeep";

    private DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    DB_URL,
                    DB_USER,
                    DB_PASSWORD);
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        }
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }
}

import java.sql.*;

/**
 * Database connection utility for the Student Management System.
 * Provides a method to establish a connection to the MySQL database.
 */
public class DatabaseConnector {

    private static Connection connection = null;

    /**
     * Establishes and returns a connection to the MySQL database.
     *
     * @return Connection object to interact with the database
     * @throws ClassNotFoundException if JDBC driver class is not found
     * @throws SQLException           if database access fails
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String dbName = "studata";        // Database name
        String user = "root";             // Database username
        String password = "1236";         // Database password
        String url = "jdbc:mysql://localhost:3306/" + dbName;

        // Load MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish and return connection
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}

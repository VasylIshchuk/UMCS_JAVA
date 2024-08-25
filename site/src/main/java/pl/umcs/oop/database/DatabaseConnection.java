package pl.umcs.oop.database;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Stores all database connections. The key is the connection name, and the value is the Connection object.
    private static final Map<String, Connection> connections = new HashMap<>();

    // Returns the database connection with the default name (an empty string).
    public static Connection getConnection() {
        return getConnection("");
    }

    // Returns the database connection with the given name if it exists in the map.
    static public Connection getConnection(String name) {
        return connections.get(name);
    }

    // Creates a database connection with the specified file path and the default name.
    public static void connect(String filePath) {
        connect(filePath, "");
    }

    // Creates a database connection with the specified file path and stores it under the given name.
    public static void connect(String filePath, String connectionName) {
        try {
            // Creates a database connection to an SQLite database using JDBC.
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            //  "jdbc:" is a protocol that indicates that we are using JDBC (Java Database Connectivity) to connect to the database.
            //  "sqlite:" is a subprotocol that indicates the type of database we are connecting to. In this case it is SQLite.
            //  "filePath"  is the path to the database file.

            // Stores the connection in the map with the connectionName as the key.
            connections.put(connectionName, connection);
            System.out.println("Connection to pl.umcs.oop.database has been established :)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Closes the database connection with the default name.
    public static void disconnect() {
        disconnect("");
    }

    // Closes the database connection with the specified name.
    public static void disconnect(String connectionName) {
        try {
            // Retrieves the connection from the map by name.
            Connection connection = connections.get(connectionName);
            // Closes the connection.
            connection.close();
            // Removes the connection from the map.
            connections.remove(connectionName);
            System.out.println("Disconnection from pl.umcs.oop.database has been established :(");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

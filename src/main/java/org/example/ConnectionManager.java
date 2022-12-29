package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    public static Connection connect() {

        Connection connection = null;
        try {
            String connectionString = "jdbc:sqlite:C:/Users/karin/IdeaProjects/Lesson-17/data/chinook.db";
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Connection to SQLite has been established");

        } catch (SQLException e) {
            System.out.println("Error while establishing connection");
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void close(Connection connection) {
        try {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("Connection to SQLite has been closed.");
            } else {
                System.out.println("No connection active");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }
}

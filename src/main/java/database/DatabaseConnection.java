package database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String PATH_TO_LOGIN = "connectionDetails.txt";
    private static String localHost;
    private static String username;
    private static String password;

    public static void setConnectionDetails() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_LOGIN))) {
            localHost = br.readLine();
            username = br.readLine();
            password = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println(localHost);
        System.out.println(username);
        System.out.println(password);

        try {
                return DriverManager.getConnection(localHost, username, password);
            } catch (SQLException e) {
                System.out.println(STR."Unable to connect to database: \{e.getMessage()}");
                throw e;

        }
    }

}

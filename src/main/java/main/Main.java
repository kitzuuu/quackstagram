package main;

import database.DatabaseConnection;
import login.ui.SignIn;

import javax.swing.*;
import java.sql.SQLException;

public class Main extends JFrame {
    public static void main(String[] args) {
        // @TODO testeaza aplicatia kurwo
        SwingUtilities.invokeLater(() -> {
            try {
                // Establish database connection
                long startTime = System.currentTimeMillis();
                System.out.println("Connecting to database . . . . . . ");
                DatabaseConnection.setConnectionDetails();
                DatabaseConnection.getConnection();
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                System.out.println(STR."Database connection successfully established in \{elapsedTime} ms");

            } catch (SQLException e) {
                System.out.println(STR."Error in connecting to database: \{e.getMessage()}");
                JOptionPane.showMessageDialog(null, "Connection to database failed",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            SignIn frame = new SignIn();
            frame.setVisible(true);
        });
    }
}

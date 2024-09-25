package ui.components.readUserBio;

import database.DatabaseConnection;
import ui.components.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class readUserBio {

    private static String bio = "";
    // keyword: changePath
    String bioDetailsFilePath = "src/main/resources/user/credentials.txt";


    public readUserBio(User currentUser) {
        readBio(currentUser);
    }

    public static String getBio() {
        return bio;
    }

    private void readBio(User currentUser) {
        try(Connection conn = DatabaseConnection.getConnection()){
            String query = "SELECT bio FROM user WHERE user_name = '" + currentUser.getUsername() + "'";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bio = rs.getString("bio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Read user bio Query failed: " + e.getMessage());
        }
    }
}

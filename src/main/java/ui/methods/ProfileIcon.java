package ui.methods;

import database.DatabaseConnection;
import ui.components.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileIcon {

    public static ImageIcon createProfileIcon(User currentUser, int PROFILE_IMAGE_SIZE) {
        Image originalImage = imageQuery(currentUser.getUsername());
        if (originalImage == null) {
            // Handle cases where no profile pic
            System.out.println("No image available for this user.");
            return null;
        }
        Image scaledImage = originalImage.getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private static Image imageQuery(String username) {
        String query = "SELECT profile_picture FROM user WHERE User_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement prepareStmt = conn.prepareStatement(query)) {
            prepareStmt.setString(1, username);
            try (ResultSet resultSet = prepareStmt.executeQuery()) {
                if (resultSet.next()) {
                    try (InputStream input = resultSet.getBinaryStream("profile_picture")) {
                        return ImageIO.read(input);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error getting the image: " + e.getMessage());
        }
        return null;
    }
}

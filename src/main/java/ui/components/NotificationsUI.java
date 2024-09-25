package ui.components;

import database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// main.NotificationsUI class extends main.BaseUI to provide a user interface for displaying notifications to the user
public class NotificationsUI extends BaseUI {
    public NotificationsUI() {
        super("Notifications"); // Calls the main.BaseUI constructor with the window title "Notifications"
        initializeUI(); // Initialize the UI components specific to main.NotificationsUI
    }

    private void initializeUI() {
        JPanel headerPanel = createHeaderPanel("Notifications 🐥"); // Create and add the header panel with the title "Notifications"
        JPanel contentPanel = createContentPanel(); // Create the content panel for displaying notifications
        JScrollPane scrollPane = createScrollPane(contentPanel); // Wrap the content panel in a scroll pane for scrolling

        int loggedUserID = ProfileUI.loggedUser.getUserID(); // Read the current user's username to filter notifications
        populateContentPanel(loggedUserID, contentPanel); // Populate the content panel with notifications relevant to the current user

        add(headerPanel, BorderLayout.NORTH); // Add the header panel at the top
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane with the content panel in the center
    }

    // Creates a scroll pane that contains the content panel
    private JScrollPane createScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    // Populates the content panel with notifications relevant to the current user
    private void populateContentPanel(int loggedUserID, JPanel contentPanel) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query ="SELECT \n" +
                    "lp.post_id, \n" +
                    "rpi.like_id, \n" +
                    "CONCAT(DATE_FORMAT(rpi.date, '%Y-%m-%d'), ' ', DATE_FORMAT(rpi.time, '%H:%i:%s')) AS datetime_of_interaction, \n" +
                    "u.user_name AS `Person who liked`\n" +
                    "FROM record_post_interaction rpi\n" +
                    "JOIN likes_post lp ON rpi.like_id = lp.like_id\n" +
                    "JOIN post p ON lp.post_id = p.post_id\n" +
                    "JOIN user u ON u.user_id = rpi.user_id\n" +
                    "WHERE p.user_id = ?\n" +
                    "ORDER BY rpi.date DESC, rpi.time DESC\n" +
                    "LIMIT ?;";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, loggedUserID);
                    // number of posts to display
                    ps.setInt(2, 10);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String userWhoLiked = rs.getString("Person who liked");
                        String timeStamp = rs.getString("datetime_of_interaction");
                        String notificationMessage = STR."\{userWhoLiked} liked your picture - \{TimeCalculator.timeSince(timeStamp)} ago";

                        // Add the notification to the panel
                        JPanel notificationPanel = new JPanel(new BorderLayout());
                        notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                        JLabel notificationLabel = new JLabel(notificationMessage);
                        notificationPanel.add(notificationLabel, BorderLayout.CENTER);

                        // Add profile icon (if available) and timestamp
                        // ... (Additional UI components if needed)

                        contentPanel.add(notificationPanel);
                    }



        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(STR."Error notifications: \{e.getMessage()}");
        }
    }
}

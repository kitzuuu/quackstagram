package ui.methods;

import ui.components.User;

import javax.swing.*;
import java.awt.*;

public class statsPanel {
    public static JPanel createStatsPanel(User currentUser) {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding
        return statsPanel;
    }

    private static JLabel createStatLabel(String ignoredNumber, String ignoredText) {
        JLabel label = new JLabel(STR."<html><div style='text-align: center;'>\{ignoredNumber}<br/>\{ignoredText}</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

}

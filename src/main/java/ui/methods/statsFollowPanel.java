package ui.methods;

import javax.swing.*;

public class statsFollowPanel {
    public static JPanel createStatsFollowPanel(JPanel statsPanel, JButton followButton) {
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        return statsFollowPanel;
    }
}

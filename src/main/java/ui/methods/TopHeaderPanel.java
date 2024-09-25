package ui.methods;

import javax.swing.*;
import java.awt.*;

public class TopHeaderPanel {

    public static JPanel createTopHeaderPanel(JLabel profileImage, JPanel statsFollowPanel) {
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        return topHeaderPanel;
    }
}

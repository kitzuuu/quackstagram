package ui.methods;

import ui.components.User;

import javax.swing.*;
import java.awt.*;

public class ProfileNameLabel {

    public static JLabel createProfileNameLabel(User currentUser) {
        JLabel profileNameLabel = new JLabel(currentUser.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides
        return profileNameLabel;
    }
}

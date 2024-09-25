package ui.methods;

import ui.components.User;

import javax.swing.*;
import java.awt.*;

public class profileBio {

    public static JTextArea crateProfileBio(User currentUser) {
        JTextArea profileBio = new JTextArea(currentUser.getBio());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides
        return profileBio;
    }
}

package ui.methods;

import javax.swing.*;
import java.awt.*;

public class ProfileNameBioPanel {

    public static JPanel createProfileNameBioPanel(JLabel profileNameLabel, JTextArea profileBio) {
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));
        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);
        return profileNameAndBioPanel;


    }
}

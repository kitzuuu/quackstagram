package ui.methods;

import javax.swing.*;

public class profileImage {

    public static JLabel createProfImage(ImageIcon profileIcon) {
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return profileImage;
    }


}

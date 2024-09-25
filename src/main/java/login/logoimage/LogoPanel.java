package login.logoimage;

import javax.swing.*;
import java.awt.*;

public class LogoPanel {
    public static JPanel createLogo() {


        JLabel lblPhoto = new JLabel();
        lblPhoto.setIcon(new
                ImageIcon(new
                ImageIcon("src/main/resources/login/logoimage/DACS.png")
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);
        photoPanel.setFocusable(true);
        return photoPanel;
    }

}

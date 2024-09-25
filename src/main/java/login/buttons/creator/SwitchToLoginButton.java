package login.buttons.creator;

import javax.swing.*;
import java.awt.*;

public class SwitchToLoginButton implements IButtons {
    @Override
    public JButton createButton(ButtonType buttonType) {
        JButton jButton = new JButton();
        jButton.setText("Already have an account? Login now!");
        jButton.addActionListener(ButtonActionListener.getActionListener(buttonType));
        jButton.setForeground(Color.DARK_GRAY);
        return jButton;
    }
}

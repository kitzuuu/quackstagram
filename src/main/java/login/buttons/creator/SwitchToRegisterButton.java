package login.buttons.creator;

import javax.swing.*;
import java.awt.*;

public class SwitchToRegisterButton implements IButtons {
    @Override
    public JButton createButton(ButtonType buttonType) {
        JButton jButton = new JButton();
        jButton.setText("Don't have an account? Register now!");
        jButton.addActionListener(ButtonActionListener.getActionListener(buttonType));
        jButton.setForeground(Color.DARK_GRAY);
        return jButton;
    }
}

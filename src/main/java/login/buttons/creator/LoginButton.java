package login.buttons.creator;

import javax.swing.*;
import java.awt.*;

public class LoginButton implements IButtons {

    @Override
    public JButton createButton(ButtonType buttonType) {
        JButton jButton = new JButton();
        jButton.setText("Login");
        jButton.addActionListener(ButtonActionListener.getActionListener(buttonType));
        jButton.setForeground(Color.DARK_GRAY);

        return jButton;
    }
}

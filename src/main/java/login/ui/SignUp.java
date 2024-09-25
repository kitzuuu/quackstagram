package login.ui;

import login.buttons.creator.ButtonType;
import login.textboxes.TextFieldFactory;

import javax.swing.*;
import java.awt.*;

public class SignUp extends AbstractLoginUI {
    public static Frame frame;
    public SignUp() {
        super("Sign Up");
        frame = this;
    }

    public static Frame getFrame() {
        return frame;
    }

    @Override
    void addBioField(JPanel fieldsPanel) {
        fieldsPanel.add(Box.createVerticalStrut(20));
        fieldsPanel.add(TextFieldFactory.createTextBox(login.textboxes.Type.BIO));
    }


    @Override
    JPanel createActionPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 0, 0));
        jPanel.setBackground(Color.DARK_GRAY);
        addButton(ButtonType.REGISTER, jPanel);
        addButton(ButtonType.SWITCH_TO_LOGIN, jPanel);
        return jPanel;
    }

    @Override
    void switchMode() {
        this.dispose();
        SignIn frame = new SignIn();
        frame.setVisible(true);
    }
}

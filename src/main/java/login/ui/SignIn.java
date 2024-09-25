package login.ui;

import login.buttons.creator.ButtonType;

import javax.swing.*;
import java.awt.*;


public class SignIn extends AbstractLoginUI {
    public static Frame frame;

    public SignIn() {
        super("Sign In");
        frame = this;
    }

    public static Frame getFrame() {
        return frame;
    }

    @Override
    void addBioField(JPanel fieldsPanel) {

    }

    @Override
    JPanel createActionPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 0, 0));
        addButton(ButtonType.LOGIN, jPanel);
        addButton(ButtonType.SWITCH_TO_REGISTER, jPanel);
        jPanel.setBackground(Color.DARK_GRAY);
        return jPanel;
    }

    public void switchMode() {
        this.dispose();
        SignUp frame = new SignUp();
        frame.setVisible(true);
    }
}

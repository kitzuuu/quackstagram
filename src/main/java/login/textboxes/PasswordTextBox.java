package login.textboxes;

import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;

public class PasswordTextBox {
    public static JTextField passwordTextBox = null;


    private PasswordTextBox() {

    }
    public static String getText(){
        return passwordTextBox.getText();
    }

    public static JTextField getInstance() {
        if (passwordTextBox == null) {
            createTextBox();
        }
        passwordTextBox.setText("");
        return passwordTextBox;
    }


    public static void createTextBox() {
        passwordTextBox = new JTextField();
        PromptSupport.setPrompt("Password", passwordTextBox);
    }
}

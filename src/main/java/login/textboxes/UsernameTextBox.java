package login.textboxes;

import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;

public class UsernameTextBox {
    public static JTextField usernameTextBox = null;


    private UsernameTextBox() {

    }

    public static JTextField getInstance() {
        if (usernameTextBox == null) {
            createTextBox();
        }
        usernameTextBox.setText("");
        return usernameTextBox;
    }


    public static void createTextBox() {
        usernameTextBox = new JTextField();
        PromptSupport.setPrompt("Username", usernameTextBox);
    }
}

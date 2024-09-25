package login.textboxes;

import javax.swing.*;

public class TextFieldFactory {
    public static JTextField createTextBox(Type type) {
        return switch (type) {
            case USER -> UsernameTextBox.getInstance();
            case PASSWORD -> PasswordTextBox.getInstance();
            case BIO -> BioTextBox.getInstance();
        };
    }

}

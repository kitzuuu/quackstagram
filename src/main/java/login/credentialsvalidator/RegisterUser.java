package login.credentialsvalidator;

import login.UploadImage.UploadImageUI;
import login.textboxes.DialogLabel;
import login.ui.SignUp;
import ui.components.ProfileUI;
import ui.components.User;

import javax.swing.*;

import static login.textboxes.BioTextBox.bioTextBox;
import static login.textboxes.PasswordTextBox.passwordTextBox;
import static login.textboxes.UsernameTextBox.usernameTextBox;

public class RegisterUser extends JFrame {

    public RegisterUser() {
        register();
    }

    public static void register() {
        if (!(usernameTextBox.getText().isEmpty() || bioTextBox.getText().isEmpty() || passwordTextBox.getText().isEmpty())) {
            if (Credentials.checkUniqueUser()) {
                new UploadImageUI().uploadImageRegister();
                Credentials.registerUser();
                Credentials.registerUserInDB();
                User currentUser = new User(usernameTextBox.getText(), bioTextBox.getText(), passwordTextBox.getText());
                SignUp.frame.dispose();
                ProfileUI profileUI = new ProfileUI(currentUser, true);
                profileUI.setVisible(true);
            } else {
                DialogLabel.setDialogText("Username already exists!");
            }
        } else {
            DialogLabel.setDialogText("Fill in the informations first!");
        }
    }

}

package login.credentialsvalidator;


import login.textboxes.DialogLabel;
import login.ui.SignIn;
import ui.components.ProfileUI;
import ui.components.User;

import javax.swing.*;

import static login.credentialsvalidator.Credentials.userData;
import static login.textboxes.PasswordTextBox.passwordTextBox;
import static login.textboxes.UsernameTextBox.usernameTextBox;

public class LogUser extends JFrame {

    public LogUser() {
        login();
    }

    public static void login() {
        if (Credentials.checkCredentials()) {
            String username = usernameTextBox.getText();
            String Password = passwordTextBox.getText();
            System.out.println(Password);
            User currentUser = new User(username, userData.get(username)[0], userData.get(username)[1]);
            SignIn.frame.dispose();
            ProfileUI profileUI = new ProfileUI(currentUser, true);
            profileUI.setVisible(true);
        } else {
            DialogLabel.setDialogText("Wrong credentials");
        }
    }
}

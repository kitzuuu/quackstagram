package login.buttons.creator;

import login.credentialsvalidator.LogUser;
import login.credentialsvalidator.RegisterUser;
import login.ui.SignIn;
import login.ui.SignUp;

import java.awt.event.ActionListener;

public class ButtonActionListener {
    public static ActionListener getActionListener(ButtonType buttonType) {
        return switch (buttonType) {
            case LOGIN -> _ -> new LogUser();
            case SWITCH_TO_LOGIN -> _ -> {
                SignUp.getFrame().dispose();
                SignIn frame = new SignIn();
                frame.setVisible(true);
            };
            case SWITCH_TO_REGISTER -> _ -> {
                SignIn.getFrame().dispose();
                SignUp frame = new SignUp();
                frame.setVisible(true);

            };
            case REGISTER -> _ -> new RegisterUser();
        };
    }

}

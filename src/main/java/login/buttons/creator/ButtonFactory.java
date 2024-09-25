package login.buttons.creator;

import javax.swing.*;

public class ButtonFactory {
    public static JButton createButton(ButtonType buttonType) {
        return switch (buttonType) {
            case LOGIN -> new LoginButton().createButton(buttonType);
            case SWITCH_TO_LOGIN -> new SwitchToLoginButton().createButton(buttonType);
            case SWITCH_TO_REGISTER -> new SwitchToRegisterButton().createButton(buttonType);
            case REGISTER -> new RegisterButton().createButton(buttonType);
        };
    }
}

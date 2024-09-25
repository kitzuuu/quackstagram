package navigationpanelsingleton.buttons;

import navigationpanelsingleton.buttoncommands.Command;

import javax.swing.*;


public class ProfileButton implements INavigationButton {
    private static final String ICON_PATH = "src/main/resources/navigationpanelsingleton/buttons/ProfileButton.png";

    @Override
    public JButton createButton(Command command) {
        return NavigationButtonFactory.createButton(ICON_PATH, command);
    }
}

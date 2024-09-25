package navigationpanelsingleton.buttons;

import navigationpanelsingleton.buttoncommands.Command;

import javax.swing.*;

public class HomeButton implements INavigationButton {
    private static final String ICON_PATH = "src/main/resources/navigationpanelsingleton/buttons/HomeButton.png";

    @Override
    public JButton createButton(Command command) {
        return NavigationButtonFactory.createButton(ICON_PATH, command);
    }
}

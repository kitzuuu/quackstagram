package navigationpanelsingleton.buttons;

import navigationpanelsingleton.buttoncommands.Command;

import javax.swing.*;

public interface INavigationButton {
    JButton createButton(Command command);
}

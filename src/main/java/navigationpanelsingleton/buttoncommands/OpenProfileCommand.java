package navigationpanelsingleton.buttoncommands;

import ui.components.ProfileUI;

import javax.swing.*;

public class OpenProfileCommand implements Command {
    private final JFrame frame;

    public OpenProfileCommand(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void execute() {
        frame.dispose();
        ProfileUI frame = new ProfileUI(ProfileUI.loggedUser, false);
        frame.setVisible(true);
    }
}

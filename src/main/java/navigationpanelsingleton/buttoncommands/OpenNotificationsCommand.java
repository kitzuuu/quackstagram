package navigationpanelsingleton.buttoncommands;

import ui.components.NotificationsUI;

import javax.swing.*;

public class OpenNotificationsCommand implements Command {
    private final JFrame frame;

    public OpenNotificationsCommand(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void execute() {
        frame.dispose();
        NotificationsUI frame = new NotificationsUI();
        frame.setVisible(true);
    }
}

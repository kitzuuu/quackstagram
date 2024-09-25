package navigationpanelsingleton.buttoncommands;

import ui.components.HomeUI;

import javax.swing.*;

public class OpenHomeCommand implements Command {
    private final JFrame frame;

    public OpenHomeCommand(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void execute() {
        // Logic to open home UI
        frame.dispose();
        HomeUI frame = new HomeUI();
        frame.setVisible(true);
    }
}

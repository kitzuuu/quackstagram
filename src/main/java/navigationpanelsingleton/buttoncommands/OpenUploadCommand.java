package navigationpanelsingleton.buttoncommands;

import ui.components.UploadUI;

import javax.swing.*;

public class OpenUploadCommand implements Command {
    private final JFrame frame;

    public OpenUploadCommand(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void execute() {
        frame.dispose();
        UploadUI frame = new UploadUI();
        frame.setVisible(true);
    }
}

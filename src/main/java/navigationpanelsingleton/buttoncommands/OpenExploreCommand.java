package navigationpanelsingleton.buttoncommands;

import ui.components.ExploreUI;

import javax.swing.*;

public class OpenExploreCommand implements Command {
    private final JFrame frame;

    public OpenExploreCommand(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void execute() {
        frame.dispose();
        ExploreUI frame = new ExploreUI();
        frame.setVisible(true);
    }
}

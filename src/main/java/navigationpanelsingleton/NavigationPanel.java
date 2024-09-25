package navigationpanelsingleton;

import navigationpanelsingleton.buttoncommands.*;
import navigationpanelsingleton.buttons.ButtonType;

import javax.swing.*;
import java.awt.*;

import static navigationpanelsingleton.buttons.NavigationButtonFactory.createButton;

public class NavigationPanel implements INavigationPanel {

    static NavigationPanel instance = null;

    private NavigationPanel() {
    }

    public static NavigationPanel getInstance() {
        if (instance == null) {
            instance = new NavigationPanel();
        }
        return instance;
    }

    @Override
    public JPanel createPanel(JFrame frame) {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        addButtons(navigationPanel, frame);
        return navigationPanel;
    }

    private void addButtons(JPanel navigationPanel, JFrame frame) {
        ButtonType[] buttonTypes = ButtonType.values();
        ButtonType lastButtonType = buttonTypes[buttonTypes.length - 1];
        for (ButtonType buttonType : buttonTypes) {
            Command command = getCommandForButtonType(buttonType, frame); // Method to get the correct command
            JButton button = createButton(buttonType.getIconPath(), command);
            navigationPanel.add(button);
            if (buttonType != lastButtonType) {
                navigationPanel.add(Box.createHorizontalGlue());
            }
        }
    }

    private Command getCommandForButtonType(ButtonType type, JFrame frame) {
        return switch (type) {
            case HOME -> new OpenHomeCommand(frame);
            case EXPLORE -> new OpenExploreCommand(frame);
            case UPLOAD -> new OpenUploadCommand(frame);
            case NOTIFICATIONS -> new OpenNotificationsCommand(frame);
            case PROFILE -> new OpenProfileCommand(frame);
        };
    }
}

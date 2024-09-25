package navigationpanelsingleton.buttons;

import navigationpanelsingleton.buttoncommands.Command;

import javax.swing.*;
import java.awt.*;

public class NavigationButtonFactory {
    private static final int NAV_ICON_SIZE = 20; // Size for navigation icons

    public static JButton createButton(String iconPath, Command command) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.addActionListener(_ -> command.execute());
        return button;
    }
}

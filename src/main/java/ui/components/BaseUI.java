package ui.components;

import navigationpanelsingleton.NavigationPanel;

import javax.swing.*;
import java.awt.*;

public abstract class BaseUI extends JFrame {
    protected static final int WIDTH = 300;
    protected static final int HEIGHT = 500;

    // Constructor that sets up common properties and adds the navigation panel
    BaseUI(String title) {
        setTitle(title);
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(NavigationPanel.getInstance().createPanel(this), BorderLayout.SOUTH);
    }

    // Creates a header panel with a given title, standardizing the look across different UIs
    protected JPanel createHeaderPanel(String title) {

        // Header Panel (reuse from main.ProfileUI or customize for home page)
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = createLblRegister(title);
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;

    }

    private JLabel createLblRegister(String title) {
        JLabel lblRegister = new JLabel(title);
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        return lblRegister;
    }

    // Creates a content panel using BoxLayout for vertical arrangement, used in child classes to add custom components
    protected JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        return contentPanel;
    }

    protected void paintComponent(Graphics g) {
    }
}



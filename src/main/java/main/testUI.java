package main;

import navigationpanelsingleton.NavigationPanel;

import javax.swing.*;
import java.awt.*;

public class testUI extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    public testUI() {
        setTitle("test");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel navigationPanel = NavigationPanel.getInstance().createPanel(this);
        JPanel headerPanel = createHeaderPanel("titleTest");
        JPanel mainContentPanel = createContentPanel(); // Create the main content panel which includes the image grid
        add(headerPanel, BorderLayout.NORTH); // Add the header at the top
        add(mainContentPanel, BorderLayout.CENTER); // Add the main content in the center
        add(navigationPanel, BorderLayout.SOUTH);

    }

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

}

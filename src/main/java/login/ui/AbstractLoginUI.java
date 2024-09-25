package login.ui;

import login.buttons.creator.ButtonFactory;
import login.buttons.creator.ButtonType;
import login.logoimage.LogoPanel;
import login.textboxes.DialogLabel;
import login.textboxes.TextFieldFactory;

import javax.swing.*;
import java.awt.*;
abstract class AbstractLoginUI extends JFrame {
    protected static final int WIDTH = 300;
    protected static final int HEIGHT = 500;
    // Constructor setting basic properties of the window
    AbstractLoginUI(String title) {
        setTitle(title);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        requestFocus(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        addComponents();
    }
    private void addComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFieldsPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
    }
    JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51));
        JLabel lblHeader = new JLabel("Quackstagram 🐥");
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);
        return headerPanel;
    }

    JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        addTextFields(fieldsPanel);
        return fieldsPanel;
    }


    void addTextFields(JPanel fieldsPanel) {
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(LogoPanel.createLogo());
        fieldsPanel.add(Box.createVerticalStrut(40));
        fieldsPanel.add(DialogLabel.createDialogText());
        fieldsPanel.add(Box.createVerticalStrut(20));
        fieldsPanel.add(TextFieldFactory.createTextBox(login.textboxes.Type.USER));
        fieldsPanel.add(Box.createVerticalStrut(20));
        fieldsPanel.add(TextFieldFactory.createTextBox(login.textboxes.Type.PASSWORD));
        addBioField(fieldsPanel);
    }


    abstract void addBioField(JPanel fieldsPanel);
    abstract JPanel createActionPanel();

    abstract void switchMode();

    void addButton(ButtonType type, JPanel jPanel) {
        jPanel.add(ButtonFactory.createButton(type));
    }
}

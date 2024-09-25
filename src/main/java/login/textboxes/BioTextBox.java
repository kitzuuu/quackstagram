package login.textboxes;

import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;

public class BioTextBox {
    public static JTextField bioTextBox = null;


    private BioTextBox() {

    }

    public static JTextField getInstance() {
        if (bioTextBox == null) {
            createTextBox();
        }
        bioTextBox.setText("");
        return bioTextBox;
    }


    public static void createTextBox() {
        bioTextBox = new JTextField();
        PromptSupport.setPrompt("Bio", bioTextBox);
    }
}

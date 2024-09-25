package login.textboxes;

import javax.swing.*;
import java.awt.*;

public class DialogLabel {
    public static JLabel dialogText;
    public static JLabel createDialogText() {
        dialogText = new JLabel();
        dialogText.setAlignmentX(Component.CENTER_ALIGNMENT);
        return dialogText;
    }

    public static void setDialogText(String text) {
        dialogText.setText(text);
    }
}

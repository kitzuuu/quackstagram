package login.UploadImage;

import login.credentialsvalidator.Credentials;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static login.credentialsvalidator.Credentials.userData;
import static login.textboxes.BioTextBox.bioTextBox;
import static login.textboxes.PasswordTextBox.passwordTextBox;
import static login.textboxes.UsernameTextBox.usernameTextBox;

public class UploadImageUI extends JFrame {


    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    // for sql
    static File selectedImageFile;
    static boolean userHasSelectedImage = false;


    public void uploadImageRegister() {
        uploadAction();
    }

    public UploadImageUI() {
    }


    //Action handler for the upload button
    private void uploadAction() {
        if (usernameTextBox.getText().isEmpty() || passwordTextBox.getText().isEmpty() || bioTextBox.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter login details before Uploading image");
        } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an image file");
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
            fileChooser.addChoosableFileFilter(filter);

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fileChooser.getSelectedFile();
                userHasSelectedImage = true;
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedImageFile.getPath()).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH));
                JLabel imagePreviewLabel = createImagePreviewLabel();
                imagePreviewLabel.setIcon(imageIcon);
                JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
            }
            else{
                System.out.println("got here ");
                selectedImageFile = null;
            }
        }
    }







    // Creates the label which will hold the image preview
    private JLabel createImagePreviewLabel() {
        JLabel imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));
        return imagePreviewLabel;
    }



    public static boolean hasUserSelectedImage(){
        return userHasSelectedImage;
    }
    public static File getImageFile(){
            return selectedImageFile;
        }

    }


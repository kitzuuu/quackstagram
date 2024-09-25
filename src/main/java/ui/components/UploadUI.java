package ui.components;

import database.DatabaseConnection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// main.ImageUploadUI class extends main.BaseUI to provide a user interface for uploading images
public class UploadUI extends BaseUI {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private JLabel imagePreviewLabel;
    private JTextArea bioTextArea;
    private JButton uploadButton;

    public UploadUI() {
        super("Upload Image"); // Calls the main.BaseUI constructor with the window title "Upload Image"
        initializeUI(); // Initialize the UI components specific to main.ImageUploadUI
    }

    private void initializeUI() {
        JPanel headerPanel = createHeaderPanel("Upload Image 🐥"); // Reuse the createHeaderPanel method
        // main.Main content panel
        JPanel contentPanel = createContentPanel();

        // Image preview
        imagePreviewLabel = createImagePreviewLabel();

        // Set an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);

        // Bio text area
        bioTextArea = createBioTextArea();
        JScrollPane bioScrollPane = createBioScrollPane();
        contentPanel.add(bioScrollPane);

        // Upload button
        uploadButton = createUploadButton();
        contentPanel.add(uploadButton);


        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    //Creates the Bio scroll pane
    private JScrollPane createBioScrollPane() {
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        return bioScrollPane;
    }

    //Creates the Upload Photo button
    private JButton createUploadButton() {
        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        return uploadButton;
    }


    //Action handler for the upload button
    private void uploadAction(ActionEvent ignoredEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try(FileInputStream BLOB = new FileInputStream(selectedFile)) {
                String bioPicture = bioTextArea.getText();
                long millis = System.currentTimeMillis();
                Date date = new java.sql.Date(millis) ;
                Time time = new java.sql.Time(millis) ;
                int userID = ProfileUI.loggedUser.getUserID() ;

                imagePreviewAndLocalSave(selectedFile);
                try(Connection conn = DatabaseConnection.getConnection()){
                    String query = "INSERT INTO post (post_id , post_file, post_bio, DATE_OF_UPLOAD, TIME_OF_UPLOAD, user_id)\n" +
                            "VALUES(DEFAULT, ? , ? , ? , ? , ? )";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setBinaryStream(1, BLOB);
                    ps.setString(2,bioPicture);
                    ps.setDate(3,date);
                    ps.setTime(4,time);
                    ps.setInt(5,userID);
                    ps.executeUpdate();
//                    System.out.println("Updated executed: " + date + time + userID + bioPicture);
                }catch (SQLException e){
                    e.printStackTrace();
                    System.out.println("Upload Image Query failed" + e.getMessage());
                }
                // Change the text of the upload button
                uploadButton.setText("Upload Another Image");

                JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, STR."Error saving image: \{ex.getMessage()}", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void imagePreviewAndLocalSave(File selectedFile) throws IOException {
        String username = ProfileUI.loggedUser.getUsername();
        int imageId = getNextImageId(username);
        String fileExtension = getFileExtension(selectedFile);
        String newFileName = STR."\{username}_\{imageId}.\{fileExtension}";

        Path destPath = Paths.get("src/main/resources/user", "images", newFileName);
        Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

        // Save the bio and image ID to a text file
        saveImageInfo(STR."\{username}_\{imageId}", username, bioTextArea.getText());

        // Load the image from the saved path
        ImageIcon imageIcon = new ImageIcon(destPath.toString());

        // Check if imagePreviewLabel has a valid size
        if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
            Image image = imageIcon.getImage();

            // Calculate the dimensions for the image preview
            int previewWidth = imagePreviewLabel.getWidth();
            int previewHeight = imagePreviewLabel.getHeight();
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            double widthRatio = (double) previewWidth / imageWidth;
            double heightRatio = (double) previewHeight / imageHeight;
            double scale = Math.min(widthRatio, heightRatio);
            int scaledWidth = (int) (scale * imageWidth);
            int scaledHeight = (int) (scale * imageHeight);

            // Set the image icon with the scaled image
            imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
        }

        imagePreviewLabel.setIcon(imageIcon);
    }

    //Returns the post number for the image detail
    private int getNextImageId(String username) throws IOException {
        Path storageDir = Paths.get("src/main/resources/user", "images"); // Ensure this is the directory where images are saved
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        int maxId = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(storageDir, STR."\{username}_*")) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                int idEndIndex = fileName.lastIndexOf('.');
                if (idEndIndex != -1) {
                    String idStr = fileName.substring(username.length() + 1, idEndIndex);
                    try {
                        int id = Integer.parseInt(idStr);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException ex) {
                        // Ignore filenames that do not have a valid numeric ID
                    }
                }
            }
        }
        return maxId + 1; // Return the next available ID
    }

    //Writes the Image info in the txt file
    private void saveImageInfo(String imageId, String username, String bio) throws IOException {
        Path infoFilePath = Paths.get("src/main/resources/user/images", "image_details.txt");
        if (!Files.exists(infoFilePath)) {
            Files.createFile(infoFilePath);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (BufferedWriter writer = Files.newBufferedWriter(infoFilePath, StandardOpenOption.APPEND)) {
            writer.write(String.format("ImageID: %s, Username: %s, Bio: %s, Timestamp: %s, Likes: 0", imageId, username, bio, timestamp));
            writer.newLine();
        }

    }


    //Returns the extension of the file
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    // Creates the label which will hold the image preview
    private JLabel createImagePreviewLabel() {
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));
        return imagePreviewLabel;
    }

    //Creates the Bio input text area
    private JTextArea createBioTextArea() {
        bioTextArea = new JTextArea("Enter a caption");
        bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        return bioTextArea;
    }

    //Reads the username for image details
}

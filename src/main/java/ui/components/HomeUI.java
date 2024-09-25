package ui.components;

import database.DatabaseConnection;
import navigationpanelsingleton.NavigationPanel;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

// main.Main UI for displaying the home feed of Quakstagram, showing posts from followed users
public class HomeUI extends JFrame {
    private static final int WIDTH = 300; // Fixed width for the window
    private static final int HEIGHT = 500; // Fixed height for the window
    private static final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts
    private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button
    private final CardLayout cardLayout; // Manages layout of different panels in the UI
    private final JPanel cardPanel; // Panel that contains different "cards" or views
    private final JPanel homePanel; // Panel for the home feed
    private final JPanel imageViewPanel; // Panel for displaying individual images in full view


    // Constructor sets up the UI elements including navigation and header panels
    public HomeUI() {
        setTitle("Quakstagram Home");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        initializeUI(); // Initialize the UI components

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home"); // Start with the home view

        JPanel headerPanel = getHeaderPanel(); // Create and add the header panel
        add(headerPanel, BorderLayout.NORTH);


        // Navigation Bar
        add(NavigationPanel.getInstance().createPanel(this), BorderLayout.SOUTH);
    }

    // Returns a panel for the header, customizing its appearance
    private static JPanel getHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = createLblRegister();
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
        return headerPanel;
    }

    private static JLabel createLblRegister() {
        JLabel lblRegister = new JLabel("🐥 Quackstagram 🐥");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        return lblRegister;
    }

    // Initializes the UI components for the home feed and image view
    private void initializeUI() {
        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling
        List<Integer> listOfWhoUserFollowsID = getWhoUserFollows(ProfileUI.loggedUser.getUserID());
        populateContentPanel(contentPanel, listOfWhoUserFollowsID);
        add(scrollPane, BorderLayout.CENTER);


        // Set up the home panel

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        homePanel.add(scrollPane, BorderLayout.CENTER);


    }

    // Populates the home panel with posts from followed users
    private void populateContentPanel(JPanel panel, List<Integer> listOfWhoUserFollowsID) {
        String query = "SELECT \n" +
                "    user.user_name, \n" +
                "    post.post_id, \n" +
                "    post.user_id AS owner_id, \n"+
                "    post.post_file, \n" +
                "    post.post_bio, \n" +
                "    COUNT(likes_post.like_id) AS likes\n" +
                "FROM \n" +
                "    post \n" +
                "JOIN \n" +
                "    user ON post.user_id = user.user_id\n" +
                "LEFT JOIN \n" +
                "    likes_post ON likes_post.post_id = post.post_id\n" +
                "WHERE \n" +
                "    post.user_id = ?\n" +
                "GROUP BY \n" +
                "    post.post_id, user.user_name, post.post_file, post.post_bio;";


        for (int i : listOfWhoUserFollowsID) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, i);
                ResultSet rs = ps.executeQuery();
                // retrieved a single post at i
                while (rs.next()) {
                    // POPULATE
                    int ownerID = rs.getInt("owner_id");
                    int postId = rs.getInt("post_id");
                    String userName = rs.getString("user_name");
                    byte[] image = rs.getBytes("post_file");
                    String bio = rs.getString("post_bio");
                    int likes = rs.getInt("likes");


                    PostData postData = new PostData(postId, userName, image, bio, likes, ownerID);
                    JLabel nameLabel = new JLabel(userName);
                    nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel imageLabel = createImageLabel(postData);
                    loadDisplayImg(imageLabel, postData);

                    JLabel descriptionLabel = descLabel(postData);

                    JLabel likesLabel = createLikesLabel(postData);

                    JButton likeButton = createLikeButton(postData.getLikes(), likesLabel, postData);

                    JPanel itemPanel = createItemPanel(nameLabel, imageLabel, descriptionLabel, likesLabel, likeButton);

                    panel.add(itemPanel);

                    JPanel spacingPanel = createSpacingPanel();
                    panel.add(spacingPanel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Populate content panel query failed: " + e.getMessage());
            }
        }
    }

    private JPanel createSpacingPanel() {
        JPanel spacingPanel = new JPanel();
        spacingPanel.setPreferredSize(new Dimension(WIDTH - 10, 5)); // Set the height for spacing
        spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
        return spacingPanel;
    }

    private JPanel createItemPanel(JLabel nameLabel, JLabel imageLabel, JLabel descriptionLabel, JLabel likesLabel, JButton likeButton) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        itemPanel.setAlignmentX(CENTER_ALIGNMENT);
        itemPanel.add(nameLabel);
        itemPanel.add(imageLabel);
        itemPanel.add(descriptionLabel);
        itemPanel.add(likesLabel);
        itemPanel.add(likeButton);
        return itemPanel;

    }

    private JLabel createLikesLabel(PostData postData) {
        JLabel likesLabel = new JLabel(String.valueOf(postData.getLikes()));
        likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return likesLabel;
    }

    private JLabel descLabel(PostData postData) {
        JLabel descriptionLabel = new JLabel(postData.getPostBio());
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return descriptionLabel;
    }

    private void loadDisplayImg(JLabel imageLabel, PostData postData) {
        try {
            byte[] imageBytes = postData.getImage();
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage originalImage = ImageIO.read(bais);
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), IMAGE_WIDTH), Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            imageLabel.setText("Image not found");
        }
    }
    private JButton createLikeButton(int image_id, JLabel likesLabel, PostData postData) {
        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.addActionListener(_ -> handleLikeAction(postData));
        return likeButton;
    }

    private JLabel createImageLabel(PostData postData) {
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Make the image clickable
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayImage(postData); // Call a method to switch to the image view
            }
        });// Add border to image label
        return imageLabel;
    }

    // Handles the action of liking a post
    private void handleLikeAction(PostData postData) {
        int loggedUserID = ProfileUI.loggedUser.getUserID();
        int likedPictureID = postData.getPostId();

        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        Time time = new Time(currentTimeMillis);
        try(Connection conn = DatabaseConnection.getConnection()){
            String call = "CALL `quackstagram`.`likePost`(?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareCall(call);
            ps.setInt(1, likedPictureID);
            ps.setInt(2, loggedUserID);
            ps.setDate(3, date);
            ps.setTime(4, time);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) { // error code in both trigger and transaction
                JOptionPane.showMessageDialog(null, "You have already liked this photo.", "Like Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Like procedure failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();

            }
        }
    }



    // Generates a list to which it will be queried by
    private List<Integer> getWhoUserFollows(int loggedUserID) {
        List<Integer> whoUserFollows = new ArrayList<>();
        String query = "SELECT user_id FROM followers WHERE followed_by = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, loggedUserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int followsUserID = rs.getInt("user_id");
                whoUserFollows.add(followsUserID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Query retrieve who follows user failed: " + e.getMessage());
        }
        return whoUserFollows;
    }

    // Displays an individual image in full view
    private void displayImage(PostData postData) {
        imageViewPanel.removeAll(); // Clear previous content


        String imageBio = new File(postData.getPostBio()).getName().split("\\.")[0];
        JLabel likesLabel = new JLabel(String.valueOf(postData.getLikes())); // Update this line

        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);


        try {
            ByteArrayInputStream inStreambj = new ByteArrayInputStream(postData.getImage());
            BufferedImage originalImage = ImageIO.read(inStreambj);
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), WIDTH - 20), Math.min(originalImage.getHeight(), HEIGHT - 40));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            fullSizeImageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            fullSizeImageLabel.setText("Image not found");
        }

        //main.User Info
        JLabel userName = createUserName(postData.getUserName());
        JPanel userPanel = createUserPanel(userName); //main.User Name

        JButton likeButton = createLikeButton(postData.getLikes(), likesLabel, postData);
        // this one needs refresh IG IDK
        likeButton.addActionListener(_ -> {
            refreshDisplayImage(postData, imageBio); // Refresh the view
        });

        // Information panel at the bottom
        JPanel infoPanel = createInfoPanel(likeButton, postData.getUserName(), postData.getPostBio());


        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel, BorderLayout.NORTH);
        imageViewPanel.revalidate();
        imageViewPanel.repaint();


        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private JPanel createUserPanel(JLabel userName) {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.add(userName);
        return userPanel;
    }

    private JLabel createUserName(String userName) {
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        return userNameLabel;
    }

    private JPanel createInfoPanel(JButton likeButton, String user, String bio) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(user)); // Description
        infoPanel.add(new JLabel(bio)); // Likes
        infoPanel.add(likeButton);
        return infoPanel;

    }

    // Refreshes the display of an image, useful for updating like counts
    private void refreshDisplayImage(PostData postData, String imageBio) {

        displayImage(postData);
    }


}

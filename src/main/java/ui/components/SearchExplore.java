package ui.components;

import database.DatabaseConnection;
import navigationpanelsingleton.NavigationPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchExplore extends BaseUI {
    private static final int IMAGE_SIZE = 100;
    private String input;
    private HashMap<String, List<Integer>> map = new HashMap<>();

    public SearchExplore(String input) throws IOException {
        super("Searched \"" + input + "\"");
        this.input = input;
        map = searchExplore(input);
        initializeUI();
    }

    private static List<Integer> searchByBio(String input) {
        List<Integer> okBios = new ArrayList<>();
        String query = "SELECT post_id FROM post WHERE post_bio LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + input + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                okBios.add(rs.getInt("post_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return okBios;
    }

    private void initializeUI() throws IOException {
        JPanel gridPanel = new JPanel();
        JPanel buttonsPanel = createButtonsPanel(gridPanel);
        JTextArea showInput = createInputTextArea();
        buttonsPanel.add(showInput);
        add(buttonsPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(gridPanel);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(scrollPane);
        add(mainContentPanel, BorderLayout.CENTER);
        loadUsers(gridPanel);
    }

    private JTextArea createInputTextArea() {
        JTextArea inputTextArea = new JTextArea("You searched " + input);
        inputTextArea.setEditable(false);
        inputTextArea.setSize(300, 20);
        return inputTextArea;
    }

    private JPanel createButtonsPanel(JPanel gridPanel) {
        JPanel buttonsPanel = new JPanel();
        JPanel backButtonPanel = new JPanel();
        JPanel userOrPostButtonsPanel = new JPanel();
        backButtonPanel.add(createBackButtonMain());
        userOrPostButtonsPanel.add(createUserButton(gridPanel));
        userOrPostButtonsPanel.add(createPostButton(gridPanel));
        buttonsPanel.add(backButtonPanel);
        buttonsPanel.add(userOrPostButtonsPanel);
        return buttonsPanel;
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> {
            this.dispose();
            try {
                new SearchExplore(input).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return backButton;
    }

    private static List<Integer> checkIfExist(String input) {
        List<Integer> okUsers = new ArrayList<>();
        String query = "SELECT user_id FROM user WHERE user_name LIKE ? OR bio LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + input + "%");
            ps.setString(2, "%" + input + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                okUsers.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return okUsers;
    }

    private JButton createUserButton(JPanel contentPanel) {
        JButton userButton = new JButton("Users");
        userButton.addActionListener(_ -> {
            try {
                addUsersToContent(contentPanel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return userButton;
    }

    private void addUsersToContent(JPanel contentPanel) throws IOException {
        loadUsers(contentPanel);
    }

    private JButton createBackButtonMain() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> {
            this.dispose();
            ExploreUI exploreUI = new ExploreUI();
            exploreUI.setVisible(true);
        });
        return backButton;
    }

    private void loadUsers(JPanel contentPanel) throws IOException {
        contentPanel.setLayout(new GridLayout(0, 1, 2, 2));
        contentPanel.removeAll();
        int counter = 0;
        for (Integer userID : map.get("Users")) {
            if(counter>=50){
                System.out.println(STR."Maximum users returned: \{counter}");
                break; // do not allow more than 50 queries on the database

            }
            System.out.println(userID);
            String query = "SELECT * FROM user WHERE user_id = ?";
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, userID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String bio = rs.getString("bio");
                    String username = rs.getString("user_name");

                    // conversion to image type
                    byte[] profilePictureBytes = rs.getBytes("profile_picture");
                    ImageIcon imageIcon = scaleImageIcon(profilePictureBytes);

                    // create UI
                    JPanel userPanel = new JPanel();
                    JLabel imageLabel = new JLabel(imageIcon);
                    JButton usernameButton = createUserNameLabel(username, username);
                    JTextArea profileBio = createProfileBio(bio);
                    profileBio.setLineWrap(true);
                    profileBio.setMaximumSize(new Dimension(100, 100));
                    userPanel.setMaximumSize(new Dimension(300, 100));
                    userPanel.add(imageLabel);
                    userPanel.add(profileBio);
                    userPanel.add(usernameButton);
                    contentPanel.add(userPanel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("The query with number: " + counter + " failed to retrieve user");
            }
            counter++;
        }
        revalidate();
        repaint();
    }

    private ImageIcon scaleImageIcon(byte[] profilePictureBytes) {
        try {
            if (profilePictureBytes != null && profilePictureBytes.length > 0) {
                ByteArrayInputStream bais = new ByteArrayInputStream(profilePictureBytes);
                BufferedImage profileConverted = ImageIO.read(bais);
                if (profileConverted != null) {
                    Image scaledImage = profileConverted.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JTextArea createProfileBio(String bio) {
        JTextArea profileBio = new JTextArea(bio);
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides
        return profileBio;
    }

    private JButton createPostButton(JPanel contentPanel) {
        JButton postButton = new JButton("Posts");
        postButton.addActionListener(_ -> addPostsToContent(contentPanel));
        return postButton;
    }

    private void addPostsToContent(JPanel contentPanel) {
        contentPanel.setLayout(new GridLayout(0, 3, 2, 2));
        loadImages(contentPanel);
    }

    private void loadImages(JPanel contentPanel) {
        contentPanel.removeAll();
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
                "    post.post_id LIKE ?\n" +
                "GROUP BY \n" +
                "    post.post_id, user.user_name, post.post_file, post.post_bio;";

        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);
            List<Integer> postIds = map.get("Posts");
            for (Integer postId : postIds) {
                ps.setInt(1,  postId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int ownerID = rs.getInt("owner_id");
                    String userName = rs.getString("user_name");
                    byte[] image = rs.getBytes("post_file");
                    String bio = rs.getString("post_bio");
                    int likes = rs.getInt("likes");

                    PostData postData = new PostData(postId, userName, image, bio, likes,ownerID);
                    ImageIcon imageIcon = scaleImageIcon(image);
                    JLabel imageLabel = new JLabel(imageIcon);
                    imageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            displayImage(postData);
                        }
                    });
                    contentPanel.add(imageLabel);
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        revalidate();
        repaint();

    }

    private JPanel containerPanel(JLabel imageLabel, JPanel topPanel, JPanel bottomPanel) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);
        return containerPanel;
    }

    private JPanel createBackButtonPanel(JButton backButton) {
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.add(backButton);
        return backButtonPanel;
    }

    private JLabel createLikesLabel(int likes) {
        return new JLabel("Likes: " + likes);
    }

    // Creates the bio text area for pictures
    private JTextArea createTextArea(String bio) {
        JTextArea bioTextArea = new JTextArea(bio);
        bioTextArea.setEditable(false);
        return bioTextArea;
    }

    // Creates the bottom panel for zoomed pictures
    private JPanel createBottomPanel(JTextArea bioTextArea, JLabel likesLabel) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);
        return bottomPanel;
    }

    // Creates the Label where the images are displayed
    private JLabel createImageLabel(PostData post) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(post.getImage());
            BufferedImage originalImage = ImageIO.read(inputStream);
            ImageIcon imageIcon = new ImageIcon(originalImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
        }
        return imageLabel;
    }

    // Creates the Panel where the username and time since posting for each picture are displayed
    private JPanel createTopPanel(JButton usernameLabel, JLabel timeLabel) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);
        return topPanel;
    }

    // Creates the label that shows time since posting
    private JLabel createTimeLabel(String timeSincePosting) {
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        return timeLabel;
    }

    private void displayImage(PostData post) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Extract image ID from the imagePath
        int imageId = post.getPostId();
        // Read image details
        String username = post.getUserName();
        String bio = post.getPostBio();
        String timeStampString = extractTimeStamp(imageId);
        int likes = 0;

        // Calculate time since posting
        String timeSincePosting = TimeCalculator.timeSince(timeStampString);

        final String finalUsername = username;

        // Top panel for username and time since posting
        JButton usernameLabel = createUserNameLabel(username, finalUsername);
        JLabel timeLabel = createTimeLabel(timeSincePosting);
        JPanel topPanel = createTopPanel(usernameLabel, timeLabel);

        // Prepare the image for display
        JLabel imageLabel = createImageLabel(post);

        // Bottom panel for bio and likes
        JTextArea bioTextArea = createTextArea(bio);
        JLabel likesLabel = createLikesLabel(likes);
        JPanel bottomPanel = createBottomPanel(bioTextArea, likesLabel);

        // Adding the components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Re-add the header and navigation panels
        add(createHeaderPanel("Search 🐥"), BorderLayout.NORTH);
        add(NavigationPanel.getInstance().createPanel(this), BorderLayout.SOUTH);

        // Panel for the back button
        JButton backButton = createBackButton();
        JPanel backButtonPanel = createBackButtonPanel(backButton);

        // Container panel for image and details
        JPanel containerPanel = containerPanel(imageLabel, topPanel, bottomPanel);

        // Add the container panel and back button panel to the frame
        add(backButtonPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private String extractTimeStamp(int imageId) {
        String query = "SELECT DATE_OF_UPLOAD, TIME_OF_UPLOAD FROM post WHERE post_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, imageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Date date = rs.getDate("DATE_OF_UPLOAD");
                Time time = rs.getTime("TIME_OF_UPLOAD");

                // Combine date and time into a single Timestamp
                Timestamp timestamp = new Timestamp(date.getTime() + time.getTime());

                // Format timestamp into "yyyy-MM-dd HH:mm:ss"
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return dateFormat.format(timestamp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, List<Integer>> searchExplore(String input) {
        // Now stores user IDs for faster lookup
        HashMap<String, List<Integer>> map = new HashMap<>();
        map.put("Users", checkIfExist(input));
        map.put("Posts", searchByBio(input));
        return map;
    }

    // Creates a Button that allows you to go to the user's profile
    private JButton createUserNameLabel(String username, String finalUsername) {
        JButton userNameLabel = new JButton(username);
        userNameLabel.addActionListener(_ -> {
            User user = new User(finalUsername); // Assuming main.User class has a constructor that takes a username
            ProfileUI profileUI = new ProfileUI(user, false);
            profileUI.setVisible(true);
            dispose(); // Close the current frame
        });
        return userNameLabel;
    }
}

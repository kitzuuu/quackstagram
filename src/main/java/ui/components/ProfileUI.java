package ui.components;

import database.DatabaseConnection;
import navigationpanelsingleton.NavigationPanel;
import ui.components.readFollowersFollowing.readFollowingFollowers;
import ui.components.readUserBio.readUserBio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Stream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Date;
import java.sql.Time;

import static ui.components.HandleImageProcessingProfileUI.ProcessNumberOfPosts.getNumberOfPosts;
import static ui.components.readFollowersFollowing.readFollowingFollowers.getFollowersCount;
import static ui.components.readFollowersFollowing.readFollowingFollowers.getFollowingCount;
import static ui.components.readUserBio.readUserBio.getBio;
import static ui.methods.ProfileIcon.createProfileIcon;
import static ui.methods.ProfileNameBioPanel.createProfileNameBioPanel;
import static ui.methods.ProfileNameLabel.createProfileNameLabel;
import static ui.methods.TopHeaderPanel.createTopHeaderPanel;
import static ui.methods.customizeFlwButton.customizeFollowButton;
import static ui.methods.profileBio.crateProfileBio;
import static ui.methods.profileImage.createProfImage;
import static ui.methods.scrollPane.createScrollPane;
import static ui.methods.statsFollowPanel.createStatsFollowPanel;
import static ui.methods.statsPanel.createStatsPanel;

public class ProfileUI extends BaseUI {
    private static final int WIDTH = 300;
    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
    private final JPanel contentPanel; // Panel to display the image grid or the clicked image
    private final JPanel headerPanel;   // Panel for the header
    public static User loggedUser = null;
    static User currentUser = null; // main.User object to store the current user's information
    boolean isCurrentUser = false;
    String bio = "";
    private int imageCount = 0;
    private int followersCount = 0;
    private int followingCount = 0;

    // File paths constants
    private static final String FOLLOWING_FILE_PATH = "src/main/resources/user/following.txt";
    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/user/images";

    public ProfileUI(User user, boolean firstOpen) {

        super("DACS Profile");
        if (firstOpen) {
            loggedUser = user;
            currentUser = user;
        } else {
            currentUser = user;
        }
        imageCount = getNumberOfPosts(currentUser, firstOpen);
        new readFollowingFollowers(currentUser);
        followersCount = getFollowersCount();
        followingCount = getFollowingCount();

        new readUserBio(currentUser);


        bio = getBio();


        setUserStats();
        contentPanel = new JPanel();
        headerPanel = createHeaderPanel();
        initializeUI();
    }

    private void setUserStats() {
        currentUser.setBio(bio);
        currentUser.setFollowersCount(followersCount);
        currentUser.setFollowingCount(followingCount);
        currentUser.setPostCount(imageCount);
    }

    private void initializeUI() {
        getContentPane().removeAll();

        add(headerPanel, BorderLayout.NORTH);
        add(NavigationPanel.getInstance().createPanel(this), BorderLayout.SOUTH);

        initializeImageGrid();
        revalidate();
        repaint();

    }

    private JPanel createHeaderPanel() {
        // Header Panel
        JPanel headerPanel = new JPanel();

        // Top Part of the Header (Profile Image, Stats, Follow Button)

        // Profile image
        ImageIcon profileIcon = createProfileIcon(currentUser, PROFILE_IMAGE_SIZE);
        JLabel profileImage = createProfImage(profileIcon);


        // Stats Panel
        JPanel statsPanel = createStatsPanel(currentUser);

        // Follow Button
        // Follow or Edit Profile Button
        JButton tempButton = new JButton();
        JButton followButton = determineButton(tempButton);
        customizeFollowButton(followButton);

        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = createStatsFollowPanel(statsPanel, followButton);

        JPanel topHeaderPanel = createTopHeaderPanel(profileImage, statsFollowPanel);
        JTextArea profileBio = crateProfileBio(currentUser);
        JLabel profileNameLabel = createProfileNameLabel(currentUser);
        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = createProfileNameBioPanel(profileNameLabel, profileBio);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);
        headerPanel.add(topHeaderPanel);
        headerPanel.add(profileNameAndBioPanel);
        return headerPanel;

    }


    private JButton determineButton(JButton followButton) { // problem
        if (currentUser.getUserID() == loggedUser.getUserID()) {
            followButton.setText("Edit Profile");
        } else {
            followButton.setText("Follow");

            // check if current user is already following
            Boolean isFollowing = checkFollowing(currentUser.getUserID(), loggedUser.getUserID());
            if(isFollowing){
                followButton.setText("Following");
            }
            followButton.addActionListener(_ -> {
                handleFollowAction(currentUser.getUserID(), loggedUser.getUserID());
                followButton.setText("Following");
            });
        }

        return followButton;
    }

    private Boolean checkFollowing(int userID, int followedByID) {
        String query = "SELECT user_id, followed_by FROM followers WHERE user_id = ? AND followed_by = ?";
        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            ps.setInt(2, followedByID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){ // if one row has been returned then follows
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Check if follow Query failed: " +e.getMessage());
        }
        return false;
    }


    private void handleFollowAction(int userID, int followedByID) {
        String query = "INSERT INTO followers (action_id, user_id, followed_by, Date_followed, Time_Followed)" +
                "VALUES (DEFAULT, ? , ?, ?, ?)";

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        java.sql.Date Date = java.sql.Date.valueOf(localDate);
        java.sql.Time Time = java.sql.Time.valueOf(localTime);
        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userID); // this user
            ps.setInt(2, followedByID); // is followed by this user -> Logged user
            ps.setDate(3,Date);
            ps.setTime(4,Time);
            ps.executeUpdate();

        } catch (SQLException E){
            E.printStackTrace();
            String errorMessage = E.getMessage();
            if(errorMessage.contains("Already following")){
                JOptionPane.showMessageDialog(null, "You have already followed this user", "Cannot follow user", JOptionPane.ERROR_MESSAGE);
            }
            else if (E.getSQLState().equals("45000)")){ // JIC
                JOptionPane.showMessageDialog(null, "You have already followed this user", "Cannot follow user", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println(STR."FollowAction Query Failed: \{E.getMessage()}");
        }
    }

    private void initializeImageGrid() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        Path imageDir = Paths.get(IMAGE_UPLOAD_DIR);
        loadUserImagesFromDirectory(imageDir);

        JScrollPane scrollPane = createScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

        revalidate();
        repaint();
    }

    private void loadUserImagesFromDirectory(Path imageDir) {

        int userID = currentUser.getUserID();
        try(Connection conn = DatabaseConnection.getConnection()){
            String query = "SELECT post_file FROM post WHERE user_id=?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                byte[] imageBytes = rs.getBytes(1);
                JLabel imageLabel = getjLabel(imageBytes);
                contentPanel.add(imageLabel);
            }

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("LoadUserImagesFromDirectory Failed: " +e.getMessage());
        }
    }

    private JLabel getjLabel(byte[] imageBytes) {
        ImageIcon imageIcon = new ImageIcon(imageBytes);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayImage(scaledIcon); // Call method to display the clicked image
            }
        });
        return imageLabel;
    }

    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}

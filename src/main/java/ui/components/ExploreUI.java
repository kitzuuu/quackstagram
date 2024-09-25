package ui.components;

import database.DatabaseConnection;
import navigationpanelsingleton.NavigationPanel;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.*;


public class ExploreUI extends BaseUI {
    private static final int WIDTH = 300; // Width of the UI window, consistent with the main.BaseUI setting
    private static final int IMAGE_SIZE = WIDTH / 3; // Size of each image thumbnail in the grid
    private final String headerTitle = "Explore 🐥"; // Title for the header panel

    public ExploreUI() {
        super("Explore"); // Calls the main.BaseUI constructor with the window title "Explore"
        initializeUI(); // Initialize the UI components specific to main.ExploreUI
    }



    private void initializeUI() {
        getContentPane().removeAll(); // Clear the container before initializing
        setLayout(new BorderLayout()); // Use BorderLayout for layout management
        JPanel headerPanel = createHeaderPanel(headerTitle); // Create and add the header panel with the title
        JPanel mainContentPanel = createMainContentPanel(); // Create the main content panel which includes the image grid
        add(headerPanel, BorderLayout.NORTH); // Add the header at the top
        add(mainContentPanel, BorderLayout.CENTER); // Add the main content in the center
        add(NavigationPanel.getInstance().createPanel(this), BorderLayout.SOUTH);
        revalidate(); // Revalidate the container to apply the changes
        repaint(); // Repaint the container to update the UI
    }

    // Creates the main content panel including a search field and an image grid
    private JPanel createMainContentPanel() {
        JPanel mainContentPanel = new JPanel();
        JTextField searchField = createSearchField(); // Create a search field for user queries
        JPanel searchPanel = createSearchPanel(searchField);

        //for later

        // Image Grid, auto rows, 3 cols, 2 hor-gap,2 ver-gap
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2));

        // Load images from the uploaded folder
        loadImage(imageGridPanel);

        JScrollPane scrollPane = createScrollPane(imageGridPanel); // Create a scroll pane for the image grid

        // main.Main content panel that holds both the search bar and the image grid
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
        searchField.addActionListener(e -> {
            String input = e.getActionCommand();
            this.dispose();
            SearchExplore searchExplore = null;
            try {
                searchExplore = new SearchExplore(input.toUpperCase());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            searchExplore.setVisible(true);
        });
        return mainContentPanel;
    }

    // Creates the TextField for searching users
    private JTextField createSearchField() {
        JTextField jTextField = new JTextField();
        PromptSupport.setPrompt("Search users", jTextField);
        return jTextField;
    }

    // Created the panel that contains the searchField
    public JPanel createSearchPanel(JTextField searchField) {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        // Limit the height
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                searchField.getPreferredSize().height));
        return searchPanel;
    }

    // Creates a ScrollPane in which the images are displayed
    protected JScrollPane createScrollPane(JPanel imageGridPanel) {
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    // Loads images from the img folder
    public void loadImage(JPanel imageGridPanel) {
        // Assuming the method selectRecentPostsID() returns an array of post IDs.
        int[] postIDs = selectRecentPostsID();
        String query = "SELECT post_file FROM post WHERE post_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int postId : postIDs) {
                pstmt.setInt(1, postId); // trimite
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        byte[] imgBytes = rs.getBytes("post_file");
                        ImageIcon imageIcon = new ImageIcon(imgBytes);
                        Image image = imageIcon.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(image);
                        JLabel imageLabel = new JLabel(scaledIcon);
                        imageGridPanel.add(imageLabel);
                        imageLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                displayImage(postId); // asta
                            }
                        });
                    }
                }
            }
            imageGridPanel.revalidate();
            imageGridPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Load Images query failed: " + ex.getMessage());
        }
    }
// BELGONS TO ^^^^
//        if (imageDir.exists() && imageDir.isDirectory()) {
//            File[] imageFiles = imageDir.listFiles((_, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
//            if (imageFiles != null) {
//                for (File imageFile : imageFiles) {
//                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageFile.getPath()).getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
//                    JLabel imageLabel = new JLabel(imageIcon);
//                    imageLabel.addMouseListener(new MouseAdapter() {
//                        @Override
//                        public void mouseClicked(MouseEvent e) {
//                            displayImage(imageFile.getPath());
//                        }
//                    });
//                    imageGridPanel.add(imageLabel);
//                }
//            }
//        }

    private int[] selectRecentPostsID() {
        int numberOfPostsToBeRetrieved = 9; // change here
        long millis = System.currentTimeMillis();  // Current time in milliseconds since the Unix epoch
        Date date = new Date(millis);  // Current date
        Time time = new Time(millis);
        String query =
                "SELECT post_id \n" +
                "FROM post \n" +
                "ORDER BY ABS(TIMESTAMPDIFF(SECOND, CONCAT(DATE_OF_UPLOAD, ' ', TIME_OF_UPLOAD), CONCAT(?, ' ', ?)))\n" +
                "LIMIT ?;\n";
        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setDate(1,date);  // now
            preparedStatement.setTime(2,time);  // now
            preparedStatement.setInt(3,numberOfPostsToBeRetrieved);
            ResultSet rs = preparedStatement.executeQuery();
            int i = 0;
            int[] PostIDs = new int[numberOfPostsToBeRetrieved];
            while(rs.next()){
                PostIDs[i] = rs.getInt(1);
                i++; // index
            }
            return PostIDs;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Get closest posts query failed: "+ e.getMessage());
        }
        return null;

    }
    // @TODO fa si tu aici viata mea ca nu e grea
    // IA SQL UL
    // in display image trimite postId
    // integreaza query ul asta


    // Handles the image display when an image is clicked
    public void displayImage(int postId) {

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Add the header and navigation panels back
        add(createHeaderPanel(headerTitle), BorderLayout.NORTH);
        add(NavigationPanel.getInstance().createPanel(this), BorderLayout.SOUTH);

        String username = "";
        String bio = "";
        String timeStampString = "";
        int likes = 0;
        byte[] imgFile = new byte[0];

        String query  ="SELECT \n" +
                "    user.User_name,\n" +
                "    post.post_file,\n" +
                "    post.post_bio,\n" +
                "    CONCAT(DATE_FORMAT(post.DATE_OF_UPLOAD, '%Y-%m-%d'), ' ', DATE_FORMAT(post.TIME_OF_UPLOAD, '%H:%i:%s')) AS DATETIME_OF_UPLOAD,\n" + //concat for time calc
                "    COUNT(likes_post.like_event_id) AS NUMBER_OF_LIKES\n" +
                "FROM post\n" +
                "JOIN user ON post.user_id = user.user_id\n" +
                "LEFT JOIN likes_post ON post.post_id = likes_post.post_id\n" +
                "WHERE post.post_id = ?\n" +
                "GROUP BY post.post_id, user.User_name, post.post_file, post.post_bio, post.DATE_OF_UPLOAD, post.TIME_OF_UPLOAD;\n";

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("user_name");
                bio = rs.getString("post_bio");
                imgFile = rs.getBytes("post_file");
                likes = rs.getInt("NUMBER_OF_LIKES");
                timeStampString = rs.getString("DATETIME_OF_UPLOAD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Receive image details Query failed: " + e.getMessage());
        }

        // Calculate time since posting
        String timeSincePosting = TimeCalculator.timeSince(timeStampString);

        final String finalUsername = username;

        // Top panel for username and time since posting
        JButton usernameLabel = createUserNameLabel(username, finalUsername);
        JLabel timeLabel = createTimeLabel(timeSincePosting);
        JPanel topPanel = createTopPanel(usernameLabel, timeLabel);

        // Prepare the image for display
        JLabel imageLabel = createImageLabel(imgFile);

        // Bottom panel for bio and likes
        JTextArea bioTextArea = createTextArea(bio);
        JLabel likesLabel = createLikesLabel(likes);
        JPanel bottomPanel = createBottomPanel(bioTextArea, likesLabel);

        // Adding the components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

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

    // Creates the container panel for the explore menu
    public JPanel containerPanel(JLabel imageLabel, JPanel topPanel, JPanel bottomPanel) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);
        return containerPanel;
    }

    // Creates a back button panel for when an image is displayed
    public JPanel createBackButtonPanel(JButton backButton) {
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.add(backButton);
        return backButtonPanel;
    }

    // Creates the actual back button
    public JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(WIDTH - 20, backButton.getPreferredSize().height));
        backButton.addActionListener(_ -> {
            getContentPane().removeAll();
            add(createHeaderPanel(headerTitle), BorderLayout.NORTH);
            add(createMainContentPanel(), BorderLayout.CENTER);
            add(NavigationPanel.getInstance().createPanel(this), BorderLayout.SOUTH);
            revalidate();
            repaint();
        });

        return backButton;
    }

    // Creates the JLabel that displays like count
    public JLabel createLikesLabel(int likes) {
        return new JLabel("Likes: " + likes);
    }

    // Creates the bio text area for pictures
    public JTextArea createTextArea(String bio) {
        JTextArea bioTextArea = new JTextArea(bio);
        bioTextArea.setEditable(false);
        return bioTextArea;
    }

    // Creates the bottom panel for zoomed pictures
    public JPanel createBottomPanel(JTextArea bioTextArea, JLabel likesLabel) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);
        return bottomPanel;
    }

    // Creates the Label where the images are displayed
    public JLabel createImageLabel(byte[] imgFile) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        if (imgFile != null && imgFile.length > 0) {
            ImageIcon imageIcon = new ImageIcon(imgFile);
            Image image = imageIcon.getImage().getScaledInstance(WIDTH, WIDTH, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(image);
            imageLabel.setIcon(scaledIcon);
        } else {
            imageLabel.setText("Image not found");
        }
        return imageLabel;
    }

    // Creates the Panel where the username and time since posting for each picture are displayed
    public JPanel createTopPanel(JButton usernameLabel, JLabel timeLabel) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);
        return topPanel;
    }

    // Creates the label that shows time since posting
    public JLabel createTimeLabel(String timeSincePosting) {
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        return timeLabel;
    }

    // Creates a Button that allows you to go to the user's profile
    public JButton createUserNameLabel(String username, String finalUsername) {
        JButton userNameLabel = new JButton(username);
        userNameLabel.addActionListener(_ -> {
            User user = new User(finalUsername);
            ProfileUI profileUI = new ProfileUI(user, false);
            profileUI.setVisible(true);
            dispose(); // Close the current frame
        });

        return userNameLabel;
    }


}

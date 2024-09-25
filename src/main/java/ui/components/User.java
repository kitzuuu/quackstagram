package ui.components;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONObject;
import org.json.JSONException;

import javax.swing.*;

public class User {
    private int userID;
    private final String username; // Immutable username assigned at creation
    private String bio; // main.User bio that can be updated
    private String password; // main.User password for authentication purposes
    private int postsCount; // Count of posts made by the user
    private int followersCount; // Number of followers the user has
    private int followingCount; // Number of users this user is following
    User user;
    // Constructor for creating a new user with a username, bio, and password
    public User(String username, String bio, String password) {
        this.username = username;
        this.bio = bio;
        this.postsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
        this.password = password;
        userID = -1;
        retrieveUserFromDB(username);
    }



    private void retrieveUserFromDB(String username) {
        String query = "SELECT GetUserProfile(?) AS user_profile;";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String userProfileJson = resultSet.getString("user_profile");
                JSONObject jsonObject = new JSONObject(userProfileJson);
                userID = jsonObject.getInt("user_id");
                postsCount = jsonObject.getInt("posts_count");
                followersCount = jsonObject.getInt("followers_count");
                followingCount = jsonObject.getInt("following_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(STR."Retrieve user data query failed: \{e.getMessage()}");
            JOptionPane.showMessageDialog(null, STR."Problem in connecting to database: \{e.getMessage()}", "Database Error", JOptionPane.ERROR_MESSAGE);

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(STR."JSON parsing error: \{e.getMessage()}");
            JOptionPane.showMessageDialog(null, STR."Local error: \{e.getMessage()}", "Reading data error", JOptionPane.ERROR_MESSAGE);

        }
    }

    // Constructor for creating a user instance with only a username (used in exploreUI contexts)
    public User(String username) {
        retrieveUserFromDB(username);
        this.username = username;
    }

    // Getter methods for user details
    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public int getFollowersCount() {

        return followersCount;
    }
    public int getUserID() {
        return this.userID;
    }
    // Setter methods for followers and following counts
    public void setFollowersCount(int followersCount) {

        this.followersCount = followersCount;
    }

    public int getFollowingCount() {

        return followingCount;
    }

    public void setFollowingCount(int followingCount) {

        this.followingCount = followingCount;
    }

    public void setPostCount(int postCount) {

        this.postsCount = postCount;
    }

    // Returns a string representation of the user, typically for debugging or logging
    @Override
    public String toString() {
        return STR."\{username}:\{bio}:\{password}"; // Format as needed
    }

}

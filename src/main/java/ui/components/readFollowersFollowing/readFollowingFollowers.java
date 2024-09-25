package ui.components.readFollowersFollowing;

import database.DatabaseConnection;
import ui.components.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class readFollowingFollowers {

    private static int followingCount = 0;
    private static int followersCount = 0;

    public readFollowingFollowers(User currentUser) {
        int userID  = currentUser.getUserID();
        retrieveFollowers(userID);
        retrieveFollowing(userID);

    }

    private void retrieveFollowers(int userID) {
        String RetrieveFollowersQuery = "select COUNT(followed_by) AS follower_count \n" +
                "FROM followers\n" +
                "WHERE user_id = ?";
        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(RetrieveFollowersQuery);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                followersCount = rs.getInt("follower_count");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Retrieve follower count query failed: " + e.getMessage());
        }
    }

    private void retrieveFollowing(int userID){
        String RetrieveFollowingQuery = "SELECT COUNT(user_id) as following_count\n" +
                "from followers\n" +
                "WHERE followed_by = ?";
        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(RetrieveFollowingQuery);
            ps.setInt(1,userID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                followingCount = rs.getInt("following_count");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Retrieve following count query failed: " + e.getMessage());
        }
    }

    public static int getFollowingCount() {
        return followingCount;
    }

    public static int getFollowersCount() {
        return followersCount;
    }

}

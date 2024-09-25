package ui.components.HandleImageProcessingProfileUI;

import database.DatabaseConnection;
import ui.components.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessNumberOfPosts {

    // key: changePath
    private static int imageCount = -1;


    public static int getNumberOfPosts(User currentUser, boolean firstOpen) {
        imageCount = 0;
        int UserID = currentUser.getUserID();// use this to get set of posts
//        System.out.println("User identified: " + UserID);
        if( UserID!=-1){ // exists and not new user
            String query = "SELECT COUNT(*) AS NUMBER_OF_POSTS FROM Post WHERE user_id=?"; // merge in teorie n avem data
            try(Connection conn = DatabaseConnection.getConnection()){
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, UserID);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
//                    System.out.println("Got result set");
                    imageCount = rs.getInt(1);
                    return imageCount;
                }

            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("Number of posts query failed " + e.getMessage());
            }
        }
        return imageCount;
    }
}

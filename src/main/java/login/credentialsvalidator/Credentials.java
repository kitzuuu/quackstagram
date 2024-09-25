package login.credentialsvalidator;

import database.DatabaseConnection;
import login.UploadImage.UploadImageUI;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.HashMap;

import static login.textboxes.BioTextBox.bioTextBox;
import static login.textboxes.PasswordTextBox.passwordTextBox;
import static login.textboxes.UsernameTextBox.usernameTextBox;

public class Credentials extends JFrame {
    public static final HashMap<String, String[]> userData = new HashMap<>();

    private Credentials() {

    }


    // change to sql
    public static boolean checkCredentials() {
        if (userData.keySet().isEmpty()) {
            loadUserData();
        }

        String username = usernameTextBox.getText();
        String password = passwordTextBox.getText();
        String hashed = Hashing.hash(password);
        String query = "SELECT username, password FROM login_data where username = ?";
        try(Connection conn = DatabaseConnection.getConnection()) {
            long startTime = System.currentTimeMillis();
            PreparedStatement prepareStmt = conn.prepareStatement(query);
            prepareStmt.setString(1, username);
            ResultSet resultSet = prepareStmt.executeQuery();
            if (resultSet.next()){
                if(resultSet.getString("username").equals(username) && resultSet.getString("password").equals(hashed)){
//                    System.out.println("logged in");
                    return true;
                }
            }
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        if ( username == null) {
            return false;
        }
        if(password == null) {
            return false;
        }
    return false;
    }
    // change to sql
    public static void registerUser() {
        String username = usernameTextBox.getText();
        String bio = bioTextBox.getText();
        String query = "INSERT INTO user (user_id, user_name, bio, profile_picture) VALUES (DEFAULT, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection()) {
            long startTime = System.currentTimeMillis();
            PreparedStatement prepareStmt = conn.prepareStatement(query);
            boolean userHasSelectedImage = UploadImageUI.hasUserSelectedImage();
            if(userHasSelectedImage) {
                FileInputStream input = new FileInputStream(UploadImageUI.getImageFile());
                prepareStmt.setBinaryStream(3, input);

            }else{
                prepareStmt.setNull(3, Types.BINARY);
            }
            prepareStmt.setString(1, username);
            prepareStmt.setString(2, bio);
            prepareStmt.executeUpdate();
//            long endTime = System.currentTimeMillis();
//            long elapsedTime = endTime - startTime;
//            System.out.println("Executed update, register user in "+elapsedTime +" ms");
        }catch (SQLException e){
            System.out.println(STR."Error in connecting to database: \{e.getMessage()}");
            JOptionPane.showMessageDialog(null, "Query has failed",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public static boolean checkUniqueUser() {
        String query = "SELECT User_name FROM user WHERE user_name = ?";
        try {
            long startTime = System.currentTimeMillis();
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement prepareStmt = conn.prepareStatement(query);
            prepareStmt.setString(1, usernameTextBox.getText());
            ResultSet resultSet = prepareStmt.executeQuery();
//            long endTime = System.currentTimeMillis();
//            long elapsedTime = endTime - startTime;
//            System.out.println(STR."Query execution time: \{elapsedTime} ms");
            return !resultSet.next(); // If any row is found non-unique
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(STR."Error in connecting to database: \{e.getMessage()}");
            JOptionPane.showMessageDialog(null, "Query has failed",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void registerUserInDB() {

        String username = usernameTextBox.getText();
        String password = passwordTextBox.getText();
//        saveDummyCredentials(username,password); // for testing purposes
        String hashPassword = Hashing.hash(password);
        String query = "INSERT INTO login_data (username, password) VALUES (?, ?)";
        try(Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashPassword);
            stmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("Logging user data query failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
//    // function to save in txt so we can test users @DELETE
//    private static void saveDummyCredentials(String username, String password) {
//        String FILE_PATH = "src/main/resources/dummyUsers.txt";
//
//        try (FileWriter fw = new FileWriter(FILE_PATH, true);
//             BufferedWriter bw = new BufferedWriter(fw);
//             PrintWriter out = new PrintWriter(bw)) {
//            out.println(STR."\{username}:\{password}");
//        } catch (IOException e) {
//            System.err.println("An error occurred while writing to the file: " + e.getMessage());
//        }
//    }

    public static void loadUserData() {
//        System.out.println("Executing read data query");
        // lasa asa in plm Hashed_password
        String query = "SELECT username, password, bio from login_data left join user on username = user.user_name";
        try(Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet RS = ps.executeQuery();
            while(RS.next()) {
                String username = RS.getString("username");
                String hashedPassword = RS.getString("password");
                String bio = RS.getString("bio");
                userData.put(username, new String[]{hashedPassword, bio});
//                System.out.println("Added to map: " + username + "---" + hashedPassword + "---" + bio );
            }
        }catch (SQLException e) {
            System.out.println("Retrieve data from database failed: "+e.getMessage());
            e.printStackTrace();
        }
    }

}

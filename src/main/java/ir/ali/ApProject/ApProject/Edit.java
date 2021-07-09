package ir.ali.ApProject.ApProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit {

    private Connection connect() {
        // SQLite connection string
        String url = Connect.url;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void editToken(String token , String email) {
        String sql = "UPDATE users SET Token = ? WHERE Email = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, token);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void editBuyerID(String BuyerID , int productID) {
        String sql = "UPDATE products SET BuyerID = ? WHERE ID = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, BuyerID);
            pstmt.setInt(2, productID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
   public void addLoginCounter(String UserEmail) {
        String sql = "UPDATE users SET LoginCounter = ? WHERE Email = ?";
        Select tempSelect = new Select();
        int logincnt = 0;
       for (User tempUser:tempSelect.selectAllUsers()) {
//           System.out.println("|" + tempUser.email +"|   |"+ UserEmail + "|");
           if(tempUser.email.equals(UserEmail)) {
               System.out.println("here");
               logincnt = tempUser.loginCounter;
           }
       }
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            System.out.println("lgn cnt : " + logincnt);
            pstmt.setInt(1, logincnt + 1);
            pstmt.setString(2, UserEmail);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

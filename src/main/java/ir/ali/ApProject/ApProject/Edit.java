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

    public void editToken(String token, String email) {
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

    public void editBuyerID(String BuyerID, int productID) {
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

    public void editUserInfo(String name,String password,String phoneNum,String email) {
        String sql = "UPDATE users SET FullName = ?,Password = ?,PhoneNumber = ? WHERE Email = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2,password);
            pstmt.setString(3,phoneNum);
            pstmt.setString(4,email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void editProduct(String subject , String description,String price,String photoLink,int productID) {
        String sql = "UPDATE products SET Subject = ?,Description=?,Price=?,PhotoLink=? WHERE ID = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject);
            pstmt.setString(2, description);
            pstmt.setString(3, price);
            pstmt.setString(4, photoLink);
            pstmt.setInt(5, productID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void editProductStar(boolean Star,int productID) {
        String sql = "UPDATE products SET IsStar = ? WHERE ID = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, Star);
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
        for (User tempUser : tempSelect.selectAllUsers()) {
//           System.out.println("|" + tempUser.email +"|   |"+ UserEmail + "|");
            if (tempUser.email.equals(UserEmail)) {
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

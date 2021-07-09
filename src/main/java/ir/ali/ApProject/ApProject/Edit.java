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
        String sql = "INSERT INTO users(Token) VALUES(?) WHERE Email = (?)";

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
        String sql = "INSERT INTO products(BuyerID) VALUES(?) WHERE ID = (?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, BuyerID);
            pstmt.setInt(2, productID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("in catch");
            System.out.println(e.getMessage());
        }
    }
}

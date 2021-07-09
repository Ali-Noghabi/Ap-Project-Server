package ir.ali.ApProject.ApProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {

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


    public void insertProduct(Product product) {
        String sql = "INSERT INTO products(ID, Category , Subject , Description , Price , SellerID , BuyerID , PhotoLink ,IsStar ) VALUES(?,?,?,?,?,?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.ID);
            pstmt.setString(2, product.category);
            pstmt.setString(3, product.subject);
            pstmt.setString(4, product.description);
            pstmt.setString(5, product.price);
            pstmt.setString(6, product.sellerID);
            pstmt.setString(7, product.buyerID);
            pstmt.setString(8, product.photoLink);
            pstmt.setBoolean(9, product.isStar);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertUser(User user) {
        String sql = "INSERT INTO users(Email, FullName , password , PhoneNumber , LoginCounter , Token) VALUES(?,?,?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.email);
            pstmt.setString(2, user.name);
            pstmt.setString(3, user.password);
            pstmt.setString(4, user.phoneNum);
            pstmt.setInt(5, user.loginCounter);
            pstmt.setString(6, user.createToken());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
package ir.ali.ApProject.ApProject;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Select {

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


    public String selectAll() {
        String sql = "SELECT * FROM products";

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Product> tempProducts = new ArrayList<>();
            // loop through the result set
            while (rs.next()) {
                Product tempProduct = new Product(true);
                tempProduct.ID = rs.getInt("ID");
                tempProduct.category = rs.getString("Category");
                tempProduct.subject = rs.getString("Subject");
                tempProduct.description = rs.getString("Description");
                tempProduct.price = rs.getString("Price");
                tempProduct.sellerID = rs.getString("SellerID");
                tempProduct.buyerID = rs.getString("BuyerID");
                tempProduct.photoLink = rs.getString("PhotoLink");
                tempProduct.isStar = rs.getBoolean("IsStar");
                tempProducts.add(tempProduct);

            }

            //Write On Json
            String Return = new Gson().toJson(tempProducts);
            return Return;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";

    }
}
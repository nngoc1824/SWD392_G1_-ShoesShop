package dao;

import entites.Product;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBContext {
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM Product";
        List<Product> products = null;
        try (Connection conn = getConnection()) {
            products = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product().builder()
                        .productId(rs.getInt("productId"))
                        .productName(rs.getString("productName"))
                        .description(rs.getString("description"))
                        .price(rs.getDouble("price"))
                        .purchaseCost(rs.getDouble("purchaseCost"))
                        .status(rs.getBoolean("status") ? 1 : 0)
                        .stock(rs.getInt("stock"))
                        .image(rs.getString("image"))
                        .categoryId(rs.getInt("categoryId"))
                        .createdOn(rs.getTimestamp("createdOn"))
                        .modifiedOn(rs.getTimestamp("modifiedOn"))
                        .build();
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return products;
    }
    public List<Product> getListProductPaginate(int pageNo){
        String sql = "SELECT * FROM Product   LIMIT ?,6";

        List<Product> products = null;
        try (Connection conn = getConnection()) {
            products = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,(pageNo - 1)* 6);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product().builder()
                        .productId(rs.getInt("productId"))
                        .productName(rs.getString("productName"))
                        .description(rs.getString("description"))
                        .price(rs.getDouble("price"))
                        .purchaseCost(rs.getDouble("purchaseCost"))
                        .status(rs.getBoolean("status") ? 1 : 0)
                        .stock(rs.getInt("stock"))
                        .image(rs.getString("image"))
                        .categoryId(rs.getInt("categoryId"))
                        .createdOn(rs.getTimestamp("createdOn"))
                        .modifiedOn(rs.getTimestamp("modifiedOn"))
                        .build();
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return products;
    }
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getListProductPaginate(2);
        if (products != null) {
            for (Product product : products) {
                System.out.println(product);
            }
        } else {
            System.out.println("No products found or an error occurred.");
        }
    }
}

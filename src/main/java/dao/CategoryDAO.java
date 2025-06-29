package dao;

import entites.Category;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DBContext {
    // This class can be extended to include methods for managing categories
    // such as adding, updating, deleting, and retrieving categories.

    // Example method to get all categories (to be implemented)
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM Category";
        List<Category> categories = null;
        try (Connection conn = getConnection()) {
            categories = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category category = new Category().builder()
                        .categoryId(rs.getInt("categoryId"))
                        .categoryName(rs.getString("categoryName"))
                        .build();
                categories.add(category);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return categories;
    }
    public int addCategory(String categoryName) {
        String sql = "INSERT INTO Category (categoryName) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoryName);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về categoryId vừa insert
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1; // Thất bại
    }

    public static void main(String[] args) {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();
        if (categories != null) {
            for (Category category : categories) {
                System.out.println(category);
            }
        } else {
            System.out.println("No categories found or an error occurred.");
        }
    }
}

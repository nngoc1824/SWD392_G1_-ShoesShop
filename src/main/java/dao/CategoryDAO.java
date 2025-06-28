package dao;

import entites.Category;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

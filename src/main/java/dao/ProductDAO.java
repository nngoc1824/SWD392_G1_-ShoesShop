package dao;

import entites.Product;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBContext {
    public List<Product> getAllProducts(int categoryId, int status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (categoryId != 0) {
            sql.append(" AND categoryId = ?");
            params.add(categoryId);
        }

        if (status != -1) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        List<Product> products = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

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


    public List<Product> getListProductPaginate(int pageNo, Integer categoryId, Integer status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (categoryId != null && categoryId != 0) {
            sql.append(" AND categoryId = ?");
            params.add(categoryId);
        }

        if (status != null && status != -1) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        sql.append(" LIMIT ?, 6");
        params.add((pageNo - 1) * 6);

        List<Product> products = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

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

    public int addProduct(Product product) {
        String sql = "INSERT INTO Product (productName, description, price, purchaseCost, status, stock, image, categoryId, createdOn, modifiedOn) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setDouble(4, product.getPurchaseCost());
            stmt.setBoolean(5, product.getStatus() == 1);
            stmt.setInt(6, product.getStock());
            stmt.setString(7, product.getImage());
            stmt.setInt(8, product.getCategoryId());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Trả về productId vừa tạo
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu có lỗi
    }


    public Product getProductById(int productId) {
        String sql = "SELECT * FROM Product WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product().builder()
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProductDetail(Product product) {
        String sql = "UPDATE Product SET productName = ?, description = ?, price = ?, purchaseCost = ?, status = ?, stock = ?, image = ?, categoryId = ? WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setDouble(4, product.getPurchaseCost());
            stmt.setBoolean(5, product.getStatus() == 1);
            stmt.setInt(6, product.getStock());
            stmt.setString(7, product.getImage());
            stmt.setInt(8, product.getCategoryId());
            stmt.setInt(9, product.getProductId());
            return stmt.executeUpdate() > 0;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean disableProduct(int productId) {
        String sql = "UPDATE Product SET status = 0 WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean enableProduct(int productId) {
        String sql = "UPDATE Product SET status = 1 WHERE productId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();

// Lấy tất cả
        dao.getAllProducts(0, -1).forEach(product -> System.out.println(product.getProductName()));

// Lọc theo categoryId = 3
        dao.getAllProducts(3, -1);

// Lọc theo status = 0 (ẩn)
        dao.getAllProducts(0, 0);

// Lọc theo cả hai
        dao.getAllProducts(2, 1);

    }


}

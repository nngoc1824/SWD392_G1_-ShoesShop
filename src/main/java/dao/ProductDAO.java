package dao;

import controller.ProductController;
import entites.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBContext {
    private static final Logger log = LoggerFactory.getLogger(ProductDAO.class);
    public List<Product> getAllProducts(int categoryId, int status, String searchQuery) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (categoryId != 0) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }

        if (status != -1) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append(" AND (product_name LIKE ? OR description LIKE ?)");
            params.add("%" + searchQuery + "%");
            params.add("%" + searchQuery + "%");
        }

        List<Product> products = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (true) {
                Product product = getProductFromRs(rs);
                if (product == null) break;
                products.add(product);
            }

        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
            return null;
        }

        return products;
    }

    public List<Product> getListProductPaginate(int pageNo, Integer categoryId, Integer status, String searchQuery) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (categoryId != null && categoryId != 0) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }

        if (status != null && status != -1) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append(" AND (product_name LIKE ? OR description LIKE ?)");
            params.add("%" + searchQuery + "%");
            params.add("%" + searchQuery + "%");
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
            while (true) {
                Product product = getProductFromRs(rs);
                if (product == null) break;
                products.add(product);
            }

        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
            return null;
        }
        return products;
    }

    public int addProduct(Product product) {
        String sql = "INSERT INTO Product (product_name, description, price, purchase_cost, status, stock, image, category_id, created_on, modified_on) " +
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
                    return rs.getInt(1); // Trả về product_id vừa tạo
                }
            }

        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
        }
        return -1; // Trả về -1 nếu có lỗi
    }

    public Product getProductById(int productId) {
        String sql = "SELECT * FROM Product WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            return getProductFromRs(rs);

        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
        }
        return null;
    }

    public boolean updateProductDetail(Product product) {
        String sql = "UPDATE Product SET product_name = ?, description = ?, price = ?, purchase_cost = ?, status = ?, stock = ?, image = ?, category_id = ? WHERE product_id = ?";
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

        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
            return false;
        }
    }

    public boolean disableProduct(int productId) {
        String sql = "UPDATE Product SET status = 0 WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
            return false;
        }
    }

    public boolean enableProduct(int productId) {
        String sql = "UPDATE Product SET status = 1 WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
            return false;
        }
    }

    public Product getProductFromRs(ResultSet rs) {
        try {
            if (rs.next()) {
                return new Product().builder()
                        .productId(rs.getInt("product_id"))
                        .productName(rs.getString("product_name"))
                        .description(rs.getString("description"))
                        .price(rs.getDouble("price"))
                        .purchaseCost(rs.getDouble("purchase_cost"))
                        .status(rs.getBoolean("status") ? 1 : 0)
                        .stock(rs.getInt("stock"))
                        .image(rs.getString("image"))
                        .categoryId(rs.getInt("category_id"))
                        .createdOn(rs.getTimestamp("created_on"))
                        .modifiedOn(rs.getTimestamp("modified_on"))
                        .build();
            }
        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
//        System.out.println(dao.getProductById(7));
        List<Product> products = dao.searchProducts("Sneaker");
        if (products != null) {
            System.out.println("Found " + products.size() + " products:");
            for (Product product : products) {
                System.out.println(product);
            }
        } else {
            System.out.println("No products found.");
        }
    }

    public List<Product> searchProducts(String searchQuery) {
        String sql = "SELECT * FROM Product WHERE product_name LIKE ? OR description LIKE ?";
        List<Product> products = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");

            ResultSet rs = stmt.executeQuery();
            while (true) {
                Product product = getProductFromRs(rs);
                if (product == null) break;
                products.add(product);
            }

        } catch (Exception e) {
            log.error("SQL Error: {}", e.getMessage());
            return null;
        }
        return products;
    }

}

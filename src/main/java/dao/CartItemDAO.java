package dao;


import entites.CartItem;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO extends DBContext {

    // Lấy tất cả cart item theo cartId
    public List<CartItem> getCartItemsByCartId(int cartId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM CartItem WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (cartId != 0) {
            sql.append(" AND cartId = ?");
            params.add(cartId);
        }

        List<CartItem> items = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem().builder()
                        .cartItemId(rs.getInt("cartItemId"))
                        .cartId(rs.getInt("cartId"))
                        .productId(rs.getInt("productId"))
                        .quantity(rs.getInt("quantity"))
                        .price(rs.getDouble("price"))
                        .build();
                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return items;
    }

    // Thêm mới cart item
    public boolean insertCartItem(CartItem item) {
        String sql = "INSERT INTO CartItem (cartId, productId, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getCartId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPrice());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật số lượng và giá
    public boolean updateCartItem(CartItem item) {
        String sql = "UPDATE CartItem SET quantity = ?, price = ? WHERE cartItemId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantity());
            stmt.setDouble(2, item.getPrice());
            stmt.setInt(3, item.getCartItemId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa cart item theo cartItemId
    public boolean deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM CartItem WHERE cartItemId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tất cả cart item theo cartId
    public boolean deleteCartItemsByCartId(int cartId) {
        String sql = "DELETE FROM CartItem WHERE cartId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

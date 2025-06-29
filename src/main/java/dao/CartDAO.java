package dao;

import entites.Cart;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends DBContext {

    public List<Cart> getAllCarts(int userId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Cart WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (userId != 0) {
            sql.append(" AND userId = ?");
            params.add(userId);
        }

        List<Cart> carts = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart().builder()
                        .cartId(rs.getInt("cartId"))
                        .total(rs.getDouble("total"))
                        .quantity(rs.getInt("quantity"))
                        .userId(rs.getInt("userId"))
                        .build();
                carts.add(cart);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return carts;
    }

    public Cart getCartById(int cartId) {
        String sql = "SELECT * FROM Cart WHERE cartId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cart().builder()
                        .cartId(rs.getInt("cartId"))
                        .total(rs.getDouble("total"))
                        .quantity(rs.getInt("quantity"))
                        .userId(rs.getInt("userId"))
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertCart(Cart cart) {
        String sql = "INSERT INTO Cart (total, quantity, userId) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, cart.getTotal());
            stmt.setInt(2, cart.getQuantity());
            stmt.setInt(3, cart.getUserId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateCart(Cart cart) {
        String sql = "UPDATE Cart SET total = ?, quantity = ?, userId = ? WHERE cartId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, cart.getTotal());
            stmt.setInt(2, cart.getQuantity());
            stmt.setInt(3, cart.getUserId());
            stmt.setInt(4, cart.getCartId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteCart(int cartId) {
        String sql = "DELETE FROM Cart WHERE cart_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public static void main(String[] args) {
        CartDAO cartDAO = new CartDAO();

        // 🔹 1. Test thêm giỏ hàng
//        Cart newCart = new Cart().builder()
//                .total(0.0)
//                .quantity(0)
//                .userId(2)
//                .build();
//        boolean insertResult = cartDAO.insertCart(newCart);
//        System.out.println("Insert Cart: " + insertResult);

        // 🔹 2. Test lấy danh sách giỏ hàng theo userId
        List<Cart> carts = cartDAO.getAllCarts(2);
        System.out.println("Cart list for userId=2:");
        for (Cart c : carts) {
            System.out.println("Cart ID: " + c.getCartId() +
                    ", Total: " + c.getTotal() +
                    ", Quantity: " + c.getQuantity() +
                    ", User ID: " + c.getUserId());
        }

    }
}

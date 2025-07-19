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
        String sql = "SELECT * FROM Cart WHERE user_id = ?";
        List<Cart> carts = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setTotal(rs.getDouble("total"));
                cart.setQuantity(rs.getInt("quantity"));
                carts.add(cart);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return carts;
    }

    public Cart getCartById(int cartId) {
        String sql = "SELECT * FROM Cart WHERE cart_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setTotal(rs.getDouble("total"));
                cart.setQuantity(rs.getInt("quantity"));
                return cart;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Cart getCartByUserId(int userId) {
        String sql = "SELECT * FROM Cart WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setTotal(rs.getDouble("total"));
                cart.setQuantity(rs.getInt("quantity"));
                return cart;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertCart(Cart cart) {
        String sql = "INSERT INTO Cart (total, quantity, user_id) VALUES (?, ?, ?)";
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
        String sql = "UPDATE Cart SET total = ?, quantity = ?, user_id = ? WHERE cart_id = ?";
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

    // Test
    public static void main(String[] args) {
        CartDAO dao = new CartDAO();
        List<Cart> list = dao.getAllCarts(2);
        for (Cart c : list) {
            System.out.println("Cart ID: " + c.getCartId() +
                    ", User ID: " + c.getUserId() +
                    ", Quantity: " + c.getQuantity() +
                    ", Total: " + c.getTotal());
        }
    }
}

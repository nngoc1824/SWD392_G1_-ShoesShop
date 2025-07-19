package dao;

import entites.CartItem;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO extends DBContext {

    // Lấy 1 cart item theo cartitem_id
    public CartItem getCartItem(int cartItemId) {
        String sql = "SELECT * FROM CartItem WHERE cartitem_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return CartItem.builder()
                        .cartItemId(rs.getInt("cartitem_id"))
                        .cartId(rs.getInt("cart_id"))
                        .productId(rs.getInt("product_id"))
                        .quantity(rs.getInt("quantity"))
                        .price(rs.getDouble("price"))
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả cart item theo cart_id
    public List<CartItem> getCartItemsByCartId(int cartId) {
        String sql = "SELECT * FROM CartItem WHERE cart_id = ?";
        List<CartItem> items = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem item = CartItem.builder()
                        .cartItemId(rs.getInt("cartitem_id"))
                        .cartId(rs.getInt("cart_id"))
                        .productId(rs.getInt("product_id"))
                        .quantity(rs.getInt("quantity"))
                        .price(rs.getDouble("price"))
                        .build();
                items.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    // Thêm mới cart item
    public boolean insertCartItem(CartItem item) {
        String sql = "INSERT INTO CartItem (cart_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
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

    // Cập nhật cart item
    public boolean updateCartItem(CartItem item) {
        String sql = "UPDATE CartItem SET quantity = ?, price = ? WHERE cartitem_id = ?";
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

    // Xóa cart item theo cartitem_id
    public boolean deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM CartItem WHERE cartitem_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tất cả cart item theo cart_id
    public boolean deleteCartItemsByCartId(int cartId) {
        String sql = "DELETE FROM CartItem WHERE cart_id = ?";
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
        CartItemDAO dao = new CartItemDAO();

        // Thêm mới
        CartItem item = CartItem.builder()
                .cartId(2)
                .productId(7)
                .quantity(2)
                .price(150.0)
                .build();

        boolean inserted = dao.insertCartItem(item);
        System.out.println("Insert success? " + inserted);

        // Lấy danh sách
        List<CartItem> items = dao.getCartItemsByCartId(2);
        for (CartItem i : items) {
            System.out.println(i);
        }

        // Cập nhật
        if (!items.isEmpty()) {
            CartItem first = items.get(0);
            first.setQuantity(first.getQuantity() + 1);
            first.setPrice(first.getPrice() + 20);
            System.out.println("Update success? " + dao.updateCartItem(first));
        }

        // Xoá 1
        if (!items.isEmpty()) {
            int id = items.get(0).getCartItemId();
            System.out.println("Delete item " + id + " success? " + dao.deleteCartItem(id));
        }

        // Xoá all
        System.out.println("Delete all items for cart_id = 2? " + dao.deleteCartItemsByCartId(2));
    }
}

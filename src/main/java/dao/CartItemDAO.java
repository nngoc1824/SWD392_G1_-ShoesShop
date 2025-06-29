package dao;


import entites.CartItem;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO extends DBContext {
    // Lấy 1 cart item theo cartItemId
    public CartItem getCartItem(int cartItemId) {
        String sql = "SELECT * FROM CartItem WHERE cartItemId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartItemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return CartItem.builder()
                        .cartItemId(rs.getInt("cartItemId"))
                        .cartId(rs.getInt("cartId"))
                        .productId(rs.getInt("productId"))
                        .quantity(rs.getInt("quantity"))
                        .price(rs.getDouble("price"))
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

        public static void main(String[] args) {
            CartItemDAO dao = new CartItemDAO();

            // 1. Thêm cart item
            CartItem item = CartItem.builder()
                    .cartId(2)
                    .productId(7)
                    .quantity(2)
                    .price(150.0)
                    .build();

            boolean inserted = dao.insertCartItem(item);
            System.out.println("Insert success? " + inserted);

            // 2. Lấy danh sách cart item theo cartId
            List<CartItem> itemList = dao.getCartItemsByCartId(1);
            System.out.println("Cart items for cartId = 1:");
            for (CartItem i : itemList) {
                System.out.println(i);
            }

            // 3. Cập nhật item đầu tiên (nếu có)
            if (!itemList.isEmpty()) {
                CartItem toUpdate = itemList.get(0);
                toUpdate.setQuantity(toUpdate.getQuantity() + 1);
                toUpdate.setPrice(toUpdate.getPrice() + 10.0);
                boolean updated = dao.updateCartItem(toUpdate);
                System.out.println("Update success? " + updated);
            }

            // 4. Xoá cart item theo ID (nếu có)
            if (!itemList.isEmpty()) {
                int cartItemId = itemList.get(0).getCartItemId();
                boolean deleted = dao.deleteCartItem(cartItemId);
                System.out.println("Delete item id " + cartItemId + " success? " + deleted);
            }

            // 5. Xoá tất cả theo cartId
            boolean allDeleted = dao.deleteCartItemsByCartId(1);
            System.out.println("Delete all items for cartId = 1? " + allDeleted);
        }

}

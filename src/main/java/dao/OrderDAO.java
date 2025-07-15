package dao;

import entites.Order;

import java.sql.*;
import java.util.*;

public class OrderDAO {
    private final Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    // Tạo đơn hàng với ID tự truyền vào
    public int create(Order order) throws SQLException {
        String sql = "INSERT INTO `Order` (order_id, total_price, order_date, status, ship_address, payment_status, phone, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getOrderId());
            stmt.setDouble(2, order.getTotalPrice());
            stmt.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setBoolean(4, order.isStatus());
            stmt.setString(5, order.getShipAddress());
            stmt.setString(6, order.getPaymentStatus());
            stmt.setString(7, order.getPhone());
            if (order.getUserId() != 0) {
                stmt.setInt(8, order.getUserId());
            } else {
                stmt.setNull(8, java.sql.Types.INTEGER);
            }

            int rows = stmt.executeUpdate();
            if (rows > 0) return order.getOrderId();
        }
        return -1;
    }

    // Tìm đơn hàng theo ID
    public Optional<Order> findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM `Order` WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        }
        return Optional.empty();
    }

    // Tìm đơn hàng theo userId
    public List<Order> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM `Order` WHERE user_id = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRow(rs));
            }
        }
        return orders;
    }

    // Lấy đơn chưa thanh toán
    public List<Order> findUnpaidOrders() throws SQLException {
        String sql = "SELECT * FROM `Order` WHERE status = false OR payment_status != 'Paid'";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRow(rs));
            }
        }
        return orders;
    }

    // Cập nhật trạng thái thanh toán
    public boolean updateStatus(int orderId, boolean status, String paymentStatus) throws SQLException {
        String sql = "UPDATE `Order` SET status = ?, payment_status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, status);
            stmt.setString(2, paymentStatus);
            stmt.setInt(3, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật địa chỉ giao hàng
    public boolean updateAddress(int orderId, String newAddress) throws SQLException {
        String sql = "UPDATE `Order` SET ship_address = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newAddress);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Cập nhật số điện thoại (nếu cần)
    public boolean updatePhone(int orderId, String newPhone) throws SQLException {
        String sql = "UPDATE `Order` SET phone = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPhone);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Xoá đơn hàng
    public boolean delete(int orderId) throws SQLException {
        String sql = "DELETE FROM `Order` WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Ánh xạ từ ResultSet sang đối tượng Order
    private Order mapRow(ResultSet rs) throws SQLException {
        return Order.builder()
                .orderId(rs.getInt("order_id"))
                .totalPrice(rs.getDouble("total_price"))
                .orderDate(rs.getDate("order_date")) // DATE
                .status(rs.getBoolean("status"))
                .shipAddress(rs.getString("ship_address"))
                .paymentStatus(rs.getString("payment_status"))
                .phone(rs.getString("phone")) // thêm phone
                .userId(rs.getInt("user_id"))
                .build();
    }

}

package dao;


import entites.Order;

import java.sql.*;
import java.util.*;

public class OrderDAO {
    private final Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    //Tạo đơn hàng mới
    public int create(Order order) throws SQLException {
        String sql = "INSERT INTO orders (total_price, order_date, status, ship_address, payment_status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, order.getTotalPrice());
            stmt.setTimestamp(2, new Timestamp(order.getOrderDate().getTime()));
            stmt.setBoolean(3, order.isStatus());
            stmt.setString(4, order.getShipAddress());
            stmt.setString(5, order.getPaymentStatus());
            stmt.setInt(6, order.getUserId());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    //Tìm đơn hàng theo ID
    public Optional<Order> findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    //Tìm đơn hàng theo userId
    public List<Order> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
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

    //Lấy đơn chưa thanh toán
    public List<Order> findUnpaidOrders() throws SQLException {
        String sql = "SELECT * FROM orders WHERE status = false OR payment_status != 'Paid'";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapRow(rs));
            }
        }
        return orders;
    }

    //Cập nhật trạng thái thanh toán
    public boolean updateStatus(int orderId, boolean status, String paymentStatus) throws SQLException {
        String sql = "UPDATE orders SET status = ?, payment_status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, status);
            stmt.setString(2, paymentStatus);
            stmt.setInt(3, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    //Cập nhật địa chỉ giao hàng
    public boolean updateAddress(int orderId, String newAddress) throws SQLException {
        String sql = "UPDATE orders SET ship_address = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newAddress);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    //Xoá đơn hàng
    public boolean delete(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;
        }
    }

    //ánh xạ kết quả từ ResultSet sang Order
    private Order mapRow(ResultSet rs) throws SQLException {
        return Order.builder()
                .orderId(rs.getInt("order_id"))
                .totalPrice(rs.getDouble("total_price"))
                .orderDate(rs.getTimestamp("order_date"))
                .status(rs.getBoolean("status"))
                .shipAddress(rs.getString("ship_address"))
                .paymentStatus(rs.getString("payment_status"))
                .userId(rs.getInt("user_id"))
                .build();
    }
}
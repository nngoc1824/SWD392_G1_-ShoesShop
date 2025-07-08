package dao;

import entites.Order;
import entites.PaymentStatus;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DBContext {

    /**
     * Lấy danh sách order với phân trang, search và filter theo userId
     */
    public List<Order> getOrders(Integer userId, String search, int page, int pageSize) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT orderId, orderCode, totalPrice, orderDate, status, shipAddress, paymentStatus, userId " +
                        "FROM oss.order WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (userId != null) {
            sql.append(" AND userId = ?");
            params.add(userId);
        }
        if (search != null && !search.isBlank()) {
            sql.append(" AND (CAST(orderCode AS CHAR) LIKE ? OR shipAddress LIKE ?)");
            String term = "%" + search.trim() + "%";
            params.add(term);
            params.add(term);
        }
        sql.append(" ORDER BY orderDate DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(Order.builder()
                            .orderId(rs.getInt("orderId"))
                            .orderCode(rs.getLong("orderCode"))
                            .totalPrice(rs.getDouble("totalPrice"))
                            .orderDate(rs.getTimestamp("orderDate").toLocalDateTime())
                            .isDelivered(rs.getBoolean("status"))
                            .shipAddress(rs.getString("shipAddress"))
                            .paymentStatus(PaymentStatus.fromCode(rs.getString("paymentStatus")))
                            .userId(rs.getInt("userId"))
                            .build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Đếm tổng số order theo điều kiện filter và search
     */
    public int countOrders(Integer userId, String search) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM oss.order WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (userId != null) {
            sql.append(" AND userId = ?");
            params.add(userId);
        }
        if (search != null && !search.isBlank()) {
            sql.append(" AND (CAST(orderCode AS CHAR) LIKE ? OR shipAddress LIKE ?)");
            String term = "%" + search.trim() + "%";
            params.add(term);
            params.add(term);
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy order theo ID
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM oss.order WHERE orderId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Order.builder()
                            .orderId(rs.getInt("orderId"))
                            .orderCode(rs.getLong("orderCode"))
                            .totalPrice(rs.getDouble("totalPrice"))
                            .orderDate(rs.getTimestamp("orderDate").toLocalDateTime())
                            .isDelivered(rs.getBoolean("status"))
                            .shipAddress(rs.getString("shipAddress"))
                            .paymentStatus(PaymentStatus.fromCode(rs.getString("paymentStatus")))
                            .userId(rs.getInt("userId"))
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật order
     */
    public boolean updateOrder(Order order) {
        String sql = "" +
                "UPDATE oss.order SET orderCode = ?, totalPrice = ?, status = ?, " +
                "shipAddress = ?, paymentStatus = ? WHERE orderId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, order.getOrderCode());
            ps.setDouble(2, order.getTotalPrice());
            ps.setBoolean(3, order.getIsDelivered());
            ps.setString(4, order.getShipAddress());
            ps.setString(5, order.getPaymentStatus().getCode());
            ps.setInt(6, order.getOrderId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
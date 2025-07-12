package dao;

import entites.Order;

import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDAO extends DBContext {
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM `Order` WHERE orderId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Order.builder()
                        .orderId(rs.getInt("orderId"))
                        .totalPrice(rs.getDouble("totalPrice"))
                        .orderDate(rs.getDate("orderDate"))
                        .status(rs.getBoolean("status"))
                        .shipAddress(rs.getString("shipAddress"))
                        .paymentStatus(rs.getString("paymentStatus"))
                        .userId(rs.getInt("userId"))
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}

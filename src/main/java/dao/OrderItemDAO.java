package dao;

import entites.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderItemDAO {
    private final Connection conn;

    public OrderItemDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO OrderItem (quantity, price, order_id, product_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getQuantity());
            stmt.setDouble(2, orderItem.getPrice());
            stmt.setInt(3, orderItem.getOrderId());
            stmt.setInt(4, orderItem.getProductId());
            stmt.executeUpdate();
        }
    }
}


package service;

import dao.OrderItemDAO;
import entites.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderItemService {
    private final OrderItemDAO orderItemDAO;

    public OrderItemService(Connection conn) {
        this.orderItemDAO = new OrderItemDAO(conn);
    }

    /**
     * Lưu 1 OrderItem
     */
    public void save(OrderItem orderItem) throws SQLException {
        orderItemDAO.create(orderItem);
    }
}


package service;

import dao.OrderDAO;
import entites.Order;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(Connection conn) {
        this.orderDAO = new OrderDAO(conn);
    }

    // Lưu đơn hàng mới và trả về orderId
    public int save(Order order) throws SQLException {
        return orderDAO.create(order);
    }

    // Lấy thông tin đơn hàng theo ID
    public Optional<Order> findById(int orderId) throws SQLException {
        return orderDAO.findById(orderId);
    }

    // Cập nhật trạng thái thanh toán
    public void updateStatus(long orderId, boolean status, String paymentStatus) throws SQLException {
        orderDAO.updateStatus((int) orderId, status, paymentStatus);
    }
}
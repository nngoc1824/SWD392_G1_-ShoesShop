package service;

import dao.OrderDAO;
import entites.Order;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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

    // ✅ Lấy tất cả đơn hàng
    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    // ✅ Lấy đơn hàng với phân trang và tìm kiếm
    public List<Order> getAllOrdersWithPagination(int page, int pageSize, String searchQuery) throws SQLException {
        // Validate input parameters
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        if (searchQuery == null) searchQuery = "";

        return orderDAO.getAllOrdersWithPagination(page, pageSize, searchQuery.trim());
    }

    // ✅ Đếm tổng số đơn hàng
    public int getTotalOrderCount(String searchQuery) throws SQLException {
        if (searchQuery == null) searchQuery = "";
        return orderDAO.getTotalOrderCount(searchQuery.trim());
    }

    // ✅ Cập nhật đơn hàng hoàn chỉnh
    public boolean updateOrder(Order order) throws SQLException {
        // Basic validation
        if (order == null || order.getOrderId() <= 0) {
            throw new IllegalArgumentException("Invalid order data");
        }
        if (order.getTotalPrice() < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        if (order.getShipAddress() == null || order.getShipAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Ship address is required");
        }
        if (order.getPaymentStatus() == null || order.getPaymentStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment status is required");
        }

        return orderDAO.updateOrder(order);
    }
}
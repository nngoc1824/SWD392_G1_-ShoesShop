package controller;

import dao.OrderDAO;
import entites.Order;
import entites.PaymentStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "EditOrderController", urlPatterns = {"/manager/edit-order"})
public class EditOrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderIdStr = req.getParameter("orderId");

        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            resp.sendRedirect("/SWD392_G1_-ShoesShop/manager/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                req.setAttribute("error", "Không tìm thấy đơn hàng với ID: " + orderId);
                req.getRequestDispatcher("/manager/orders").forward(req, resp);
                return;
            }

            // Set order và payment status options để hiển thị trong form
            req.setAttribute("order", order);
            req.setAttribute("paymentStatuses", PaymentStatus.values());

            req.getRequestDispatcher("/manager_pages/edit_order.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Order ID không hợp lệ");
            req.getRequestDispatcher("/manager/orders").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String orderIdStr = req.getParameter("orderId");
        String orderCodeStr = req.getParameter("orderCode");
        String totalPriceStr = req.getParameter("totalPrice");
        String isDeliveredStr = req.getParameter("isDelivered");
        String shipAddress = req.getParameter("shipAddress");
        String paymentStatusStr = req.getParameter("paymentStatus");
        String userIdStr = req.getParameter("userId");

        try {
            // Parse và validate dữ liệu
            int orderId = Integer.parseInt(orderIdStr);
            Long orderCode = Long.parseLong(orderCodeStr);
            Double totalPrice = Double.parseDouble(totalPriceStr);
            boolean isDelivered = "on".equals(isDeliveredStr) || "true".equals(isDeliveredStr);
            PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentStatusStr);


            // Tạo Order object
            Order order = Order.builder()
                    .orderId(orderId)
                    .orderCode(orderCode)
                    .totalPrice(totalPrice)
                    .isDelivered(isDelivered)
                    .shipAddress(shipAddress)
                    .paymentStatus(paymentStatus)
                    .build();

            // Update order
            boolean success = orderDAO.updateOrder(order);

            if (success) {
                req.setAttribute("success", "Cập nhật đơn hàng thành công!");
            } else {
                req.setAttribute("error", "Có lỗi xảy ra khi cập nhật đơn hàng!");
            }

            // Reload order data để hiển thị
            Order updatedOrder = orderDAO.getOrderById(orderId);
            req.setAttribute("order", updatedOrder);
            req.setAttribute("paymentStatuses", PaymentStatus.values());

            req.getRequestDispatcher("/manager_pages/edit_order.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());

            // Reload order để hiển thị lại form
            try {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderDAO.getOrderById(orderId);
                req.setAttribute("order", order);
                req.setAttribute("paymentStatuses", PaymentStatus.values());
            } catch (Exception ex) {
                // Nếu không thể load order, redirect về list
                resp.sendRedirect("/SWD392_G1_-ShoesShop/manager/orders");
                return;
            }

            req.getRequestDispatcher("/manager_pages/edit_order.jsp").forward(req, resp);
        }
    }
}
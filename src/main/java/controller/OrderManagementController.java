package controller;

import dto.OrderDTO;
import entites.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.OrderService;
import utils.DBContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/orders", "/edit-order"})
public class OrderManagementController extends HttpServlet {
    
    private static final Logger log = LoggerFactory.getLogger(OrderManagementController.class);
    private static final int PAGE_SIZE = 10; // Number of orders per page
    private static final List<String> PAYMENT_STATUSES = Arrays.asList("Pending", "Paid", "Failed", "Refunded");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        log.info("OrderManagementController - Processing GET request for path: {}", path);
        log.info("OrderManagementController - Full request URI: {}", request.getRequestURI());

        try (Connection conn = new DBContext().getConnection()) {
            log.info("OrderManagementController - Database connection established successfully");
            OrderService orderService = new OrderService(conn);
            
            switch (path) {
                case "/orders":
                    log.info("OrderManagementController - Handling order list request");
                    handleOrderList(request, response, orderService);
                    break;
                case "/edit-order":
                    log.info("OrderManagementController - Handling edit order form request");
                    handleEditOrderForm(request, response, orderService);
                    break;
                default:
                    log.warn("OrderManagementController - Unknown path: {}", path);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            
        } catch (Exception e) {
            log.error("Error in OrderManagementController: {}", e.getMessage(), e);
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/edit-order".equals(path)) {
            try (Connection conn = new DBContext().getConnection()) {
                OrderService orderService = new OrderService(conn);
                handleUpdateOrder(request, response, orderService);
            } catch (Exception e) {
                log.error("Error updating order: {}", e.getMessage(), e);
                request.setAttribute("error", "Đã xảy ra lỗi khi cập nhật đơn hàng: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/manager_pages/edit_order.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleOrderList(HttpServletRequest request, HttpServletResponse response, OrderService orderService)
            throws ServletException, IOException, SQLException {
        
        // Get parameters
        String pageParam = request.getParameter("page");
        String searchQuery = request.getParameter("search");
        
        int currentPage = 1;
        try {
            if (pageParam != null && !pageParam.isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid page parameter: {}", pageParam);
            currentPage = 1;
        }
        
        if (searchQuery == null) searchQuery = "";
        
        // Get orders with pagination
        List<Order> orders = orderService.getAllOrdersWithPagination(currentPage, PAGE_SIZE, searchQuery);
        int totalOrders = orderService.getTotalOrderCount(searchQuery);
        int totalPages = (int) Math.ceil((double) totalOrders / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        // Convert to DTOs for JSP
        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderDTO::fromOrder)
                .collect(Collectors.toList());

        // Set attributes for JSP
        request.setAttribute("orders", orderDTOs);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", searchQuery);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/manager_pages/order_list.jsp").forward(request, response);
    }

    private void handleEditOrderForm(HttpServletRequest request, HttpServletResponse response, OrderService orderService)
            throws ServletException, IOException, SQLException {
        
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdParam);
            Optional<Order> orderOpt = orderService.findById(orderId);
            
            if (orderOpt.isPresent()) {
                OrderDTO orderDTO = OrderDTO.fromOrder(orderOpt.get());
                request.setAttribute("order", orderDTO);
                request.setAttribute("paymentStatuses", PAYMENT_STATUSES);
                request.getRequestDispatcher("/WEB-INF/manager_pages/edit_order.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Không tìm thấy đơn hàng với ID: " + orderId);
                response.sendRedirect(request.getContextPath() + "/orders");
            }
            
        } catch (NumberFormatException e) {
            log.warn("Invalid order ID parameter: {}", orderIdParam);
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }

    private void handleUpdateOrder(HttpServletRequest request, HttpServletResponse response, OrderService orderService)
            throws ServletException, IOException, SQLException {
        
        try {
            // Get form parameters
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
            boolean isDelivered = "on".equals(request.getParameter("delivered"));
            String shipAddress = request.getParameter("shipAddress");
            String paymentStatus = request.getParameter("paymentStatus");
            // Note: orderCode is ignored as it's just orderId for display
            
            // Get existing order
            Optional<Order> existingOrderOpt = orderService.findById(orderId);
            if (!existingOrderOpt.isPresent()) {
                request.setAttribute("error", "Không tìm thấy đơn hàng với ID: " + orderId);
                response.sendRedirect(request.getContextPath() + "/orders");
                return;
            }
            
            Order existingOrder = existingOrderOpt.get();
            
            // Update order fields
            Order updatedOrder = Order.builder()
                    .orderId(orderId)
                    .totalPrice(totalPrice)
                    .orderDate(existingOrder.getOrderDate()) // Keep original date
                    .status(isDelivered)
                    .shipAddress(shipAddress.trim())
                    .paymentStatus(paymentStatus)
                    .phone(existingOrder.getPhone()) // Keep original phone
                    .userId(existingOrder.getUserId()) // Keep original userId
                    .build();
            
            // Update order
            boolean success = orderService.updateOrder(updatedOrder);
            
            if (success) {
                request.setAttribute("success", "Đơn hàng đã được cập nhật thành công!");
            } else {
                request.setAttribute("error", "Không thể cập nhật đơn hàng. Vui lòng thử lại.");
            }
            
            // Reload order data and show form again
            OrderDTO updatedOrderDTO = OrderDTO.fromOrder(updatedOrder);
            request.setAttribute("order", updatedOrderDTO);
            request.setAttribute("paymentStatuses", PAYMENT_STATUSES);
            request.getRequestDispatcher("/WEB-INF/manager_pages/edit_order.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            log.warn("Invalid number format in order update: {}", e.getMessage());
            request.setAttribute("error", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
            request.getRequestDispatcher("/WEB-INF/manager_pages/edit_order.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error in order update: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/manager_pages/edit_order.jsp").forward(request, response);
        }
    }
}

package controller;

import dao.OrderDAO;
import entites.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderListController", urlPatterns = {"/manager/orders"})
public class OrderListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy parameters từ request
        String userIdStr = req.getParameter("userId");
        String search = req.getParameter("search");
        String pageStr = req.getParameter("page");

        // Parse parameters
        int page = 1;
        int pageSize = 10; // Số order trên mỗi trang
        Integer userId = null;

        try {
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            }

            if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                userId = Integer.parseInt(userIdStr);
            }
        } catch (NumberFormatException e) {
            // Nếu parse lỗi thì dùng giá trị mặc định
            page = 1;
            userId = null;
        }

        // Lấy danh sách order và tổng số order
        List<Order> orders = orderDAO.getOrders(userId, search, page, pageSize);
        int totalOrders = orderDAO.countOrders(userId, search);

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
        if (totalPages == 0) totalPages = 1;

        // Đảm bảo page không vượt quá totalPages
        if (page > totalPages) {
            page = totalPages;
            orders = orderDAO.getOrders(userId, search, page, pageSize);
        }

        // Set attributes để truyền sang JSP
        req.setAttribute("orders", orders);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("search", search);
        req.setAttribute("userId", userIdStr);
        req.setAttribute("pageSize", pageSize);

        // Debug info
        System.out.println("OrderListController - Page: " + page + ", Total Pages: " + totalPages + ", Total Orders: " + totalOrders);
        System.out.println("OrderListController - UserId: " + userId + ", Search: " + search);
        System.out.println("OrderListController - Orders found: " + orders.size());

        // Forward tới JSP
        req.getRequestDispatcher("/manager_pages/order_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redirect về GET để tránh việc resubmit form
        doGet(req, resp);
    }
}
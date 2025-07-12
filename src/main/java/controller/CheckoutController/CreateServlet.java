package controller.CheckoutController;

import entites.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.OrderService;

import java.io.IOException;

@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("orderId"));
        Order order = orderService.getOrderById(id);
        req.setAttribute("order", order);
        req.getRequestDispatcher("/checkout/create.jsp").forward(req, resp);
    }
}

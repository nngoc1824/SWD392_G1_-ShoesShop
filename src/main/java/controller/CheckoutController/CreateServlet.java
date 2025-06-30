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
    private OrderService orderService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        Order order = orderService.findByOrderId(id);
        req.setAttribute("order", order);
        req.getRequestDispatcher("/create.jsp").forward(req, resp);
    }
}

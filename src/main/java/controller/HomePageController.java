package controller;

import dao.ProductDAO;


import entites.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomePageController", urlPatterns = {"/home"})
public class HomePageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts(0,-1) ;// 0: không lọc theo category, -1: không lọc theo status

        // (Tùy chọn) Đếm số sản phẩm trong giỏ hàng từ session
        HttpSession session = request.getSession();
        Object cart = session.getAttribute("cart");
        int cartSize = (cart instanceof List) ? ((List<?>) cart).size() : 0;

        // Đưa dữ liệu vào request
        request.setAttribute("products", products);
        request.setAttribute("cartSize", cartSize);

        // Điều hướng sang trang JSP
        request.getRequestDispatcher("homepage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // xử lý POST như GET
    }
}

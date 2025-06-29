package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProductService;

import java.io.IOException;

@WebServlet("/enable-product")
public class EnableProductController extends HttpServlet {
    private ProductService productService = new ProductService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdStr = req.getParameter("id");
        if (productIdStr != null && !productIdStr.isEmpty()) {
            int productId = Integer.parseInt(productIdStr);
            // Assuming ProductDAO has a method to disable a product by its ID
            boolean isDisabled = productService.enableProduct(productId);

            if (isDisabled) {
                resp.sendRedirect("product?message=Product disabled successfully");
            } else {
                resp.sendRedirect("product?error=Failed to disable product");
            }
        } else {
            resp.sendRedirect("product?error=Invalid product ID");
        }
    }
}

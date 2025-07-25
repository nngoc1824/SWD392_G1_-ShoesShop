package controller;

import entites.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.ProductService;

import java.io.IOException;

@WebServlet("/product-detail")
public class ProductDetailController extends HttpServlet {

    private ProductService productService;
//    private SizeDAO sizeDAO;

    @Override
    public void init() {
        productService = new ProductService();
//        sizeDAO = new SizeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect("home");
                return;
            }

            int productId = Integer.parseInt(idParam);
            Product product = productService.getProductById(productId);

            if (product == null) {
                request.setAttribute("error", "Không tìm thấy sản phẩm.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }


            request.setAttribute("product", product);
            request.getRequestDispatcher("productDetail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi xử lý sản phẩm.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}

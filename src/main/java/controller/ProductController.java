package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import entites.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductController", urlPatterns = {"/product"})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageNo = req.getParameter("pageNo");
        int pageNum = 1;
        if (pageNo != null) {
            pageNum = Integer.parseInt(pageNo);
        }
        System.out.println(pageNum);
        List<Product> products = productDAO.getAllProducts();
        int pageCount = (int) Math.ceil(products.size() * 1.0 / 6);

        List<Product> productList = productDAO.getListProductPaginate(pageNum);
        req.setAttribute("currentPage",pageNum);
        req.setAttribute("pageCount", pageCount);
        req.setAttribute("productList", productList);
        req.getRequestDispatcher("/manager_pages/product_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}

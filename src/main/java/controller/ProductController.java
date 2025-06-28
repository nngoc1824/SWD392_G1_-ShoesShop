package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import entites.Category;
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
        String category = req.getParameter("category");
        String status = req.getParameter("status");

        int pageNum = 1;
        int categoryNum = 0;  // 0 means no filtering
        int statusNum = -1;   // -1 means no filtering

        try {
            if (pageNo != null && !pageNo.isEmpty()) {
                pageNum = Integer.parseInt(pageNo);
            }
            if (category != null && !category.isEmpty()) {
                categoryNum = Integer.parseInt(category);
            }
            if (status != null && !status.isEmpty()) {
                statusNum = Integer.parseInt(status);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Debug
        System.out.println("Category: " + categoryNum);
        System.out.println("Status: " + statusNum);
        System.out.println("Page: " + pageNum);

        List<Product> products = productDAO.getAllProducts(categoryNum, statusNum);
        if (products == null) {
            products = List.of();
        }
        int pageCount = (int) Math.ceil(products.size() / 6.0);
        if (pageCount == 0) pageCount = 1;
        if (pageNum > pageCount) pageNum = 1;

        List<Category> categories = categoryDAO.getAllCategories();
        List<Product> productList = productDAO.getListProductPaginate(pageNum, categoryNum, statusNum);

        req.setAttribute("categoryCrr", categoryNum);
        req.setAttribute("statusCrr", statusNum);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", pageNum);
        req.setAttribute("pageCount", pageCount);
        req.setAttribute("productList", productList);
        req.getRequestDispatcher("/manager_pages/product_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}

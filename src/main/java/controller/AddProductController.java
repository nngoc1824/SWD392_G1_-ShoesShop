package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import entites.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.CloudinaryConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@MultipartConfig
@WebServlet("/add-product")
public class AddProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO = new CategoryDAO();
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to the add product page
        req.setAttribute("categories", categoryDAO.getAllCategories());
        req.getRequestDispatcher("/manager_pages/add_product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Part imagePart = req.getPart("image");
        String name = req.getParameter("productName");
        String description = req.getParameter("description");
        int priceStr = Integer.parseInt(req.getParameter("price"));
        int categoryIdStr = Integer.parseInt(req.getParameter("category"));
        int purchaseCost = Integer.parseInt(req.getParameter("purchaseCost"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        String newCategory = req.getParameter("newCategory");
//        if(newCategory != null && !newCategory.isEmpty()) {
//            // If new category is provided, add it to the database
//            categoryDAO.addCategory(newCategory);
//            // Get the latest category ID
//            categoryIdStr = categoryDAO.getLatestCategoryId();
//        }
        String imageUrl = "";
        File tempFile = null;

        if (imagePart != null && imagePart.getSize() > 0) {
            tempFile = File.createTempFile("upload_", ".jpg");
            try (InputStream input = imagePart.getInputStream();
                 FileOutputStream output = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            imageUrl = CloudinaryConfig.updloadImage(tempFile);
        }

        System.out.println("Image URL: " + imageUrl);
        ProductDAO productDAO = new ProductDAO();
        Product product = new Product().builder()
                .productName(name)
                .description(description)
                .price(priceStr)
                .categoryId(categoryIdStr)
                .purchaseCost(purchaseCost)
                .image(imageUrl)
                .stock(stock)
                .status(1)
                .build();
        // Save the product to the database
        boolean isAdded = productDAO.addProduct(product);
        if (isAdded) {
            // Redirect to the product list page with success message
            req.setAttribute("message", "Product added successfully!");
            resp.sendRedirect(req.getContextPath() + "/product");
        } else {
            // Forward back to the add product page with error message
            req.setAttribute("error", "Failed to add product. Please try again.");
            req.getRequestDispatcher("/manager_pages/add_product.jsp").forward(req, resp);
        }
    }
}

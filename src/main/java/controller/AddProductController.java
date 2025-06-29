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
import org.json.JSONArray;
import org.json.JSONObject;
import service.CategoryService;
import service.ProductService;
import service.ProductSizeService;
import service.SizeService;
import utils.CloudinaryConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: fix the issue with adding new sizes
@MultipartConfig
@WebServlet("/add-product")
public class AddProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private SizeService sizeService = new SizeService();
    private ProductSizeService pr = new ProductSizeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to the add product page
        req.setAttribute("sizes", sizeService.getAllSizes());
        req.setAttribute("categories", categoryService.getAllCategories());
        req.getRequestDispatcher("/manager_pages/add_product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Part imagePart = req.getPart("image");
        String name = req.getParameter("productName");
        String description = req.getParameter("description");
        int priceStr = Integer.parseInt(req.getParameter("price"));
        int categoryIdStr = 0;
        if (req.getParameter("category") != null) {
            categoryIdStr = Integer.parseInt(req.getParameter("category"));
        }
        int purchaseCost = Integer.parseInt(req.getParameter("purchaseCost"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        String newCategory = req.getParameter("newCategory");
        int categoryId = 0;

        if (newCategory != null && !newCategory.isEmpty()) {
            // If new category is provided, add it to the database
            categoryId = categoryService.addCategory(newCategory);
        }
        String imageUrl = "";

        if (imagePart != null && imagePart.getSize() > 0) {

            try {
                imageUrl = CloudinaryConfig.updloadImage(imagePart);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Image URL: " + imageUrl);
        Product product = new Product().builder()
                .productName(name)
                .description(description)
                .price(priceStr)
                .categoryId(categoryIdStr > 0 ? categoryIdStr : categoryId)
                .purchaseCost(purchaseCost)
                .image(imageUrl)
                .stock(stock)
                .status(1)
                .build();
        // Save the product to the database
        int isAdded = productService.addProduct(product);
        System.out.println("Add Product: "+isAdded);
        if (isAdded != -1) {
            // Nếu thêm sản phẩm thành công, xử lý các size
            System.out.println("Product added with ID: " + isAdded);
            String json = req.getParameter("sizes");
            List<Integer> sizeIds = new ArrayList<>();
            List<String> newSizes = new ArrayList<>();

            // Tách các size từ JSON

            if (json != null && !json.isEmpty()) {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    // Nếu có 'code' thì là size cũ (đã có trong DB)
                    if (obj.has("code") && !obj.isNull("code")) {
                        int sizeId = obj.getInt("code");
                        sizeIds.add(sizeId);
                    } else {
                        // Nếu không có thì là size mới
                        String newSize = obj.getString("value");
                        newSizes.add(newSize);
                    }
                }
            }
            // sizeIds và newSizes đã được tách từ Tagify (value = sizeNumber, code = sizeId)
            List<Integer> allSizeIds = new ArrayList<>(sizeIds);
            // Thêm size mới vào DB và bổ sung sizeId
            for (String newSize : newSizes) {
                int newId = sizeService.insertSize(newSize);
                allSizeIds.add(newId);
            }
            // Thêm cái mới
            for (int newId : allSizeIds) {
                if (!pr.exists(isAdded, newId)) {
                    pr.insert(isAdded, newId);
                }
            }
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

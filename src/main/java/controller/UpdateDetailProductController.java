package controller;

import entites.Product;
import entites.Size;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import service.CategoryService;
import service.ProductService;
import service.ProductSizeService;
import service.SizeService;
import utils.CloudinaryConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@MultipartConfig
@WebServlet("/update-product")
public class UpdateDetailProductController extends HttpServlet {
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private SizeService sizeService = new SizeService();
    private ProductSizeService pr = new ProductSizeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        if (productId != null && !productId.isEmpty()) {
            try {
                int id = Integer.parseInt(productId);
                // Thêm dòng này
                req.setAttribute("sizes", sizeService.getAllSizes());
                req.setAttribute("selectedSizes", pr.getSizesByProductId(id));
                req.setAttribute("categories", categoryService.getAllCategories());
                req.setAttribute("product", productService.getProductById(id));
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
                return;
            }
            req.getRequestDispatcher("/manager_pages/update_product.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("productId"));
        int productId = Integer.parseInt(req.getParameter("productId"));
        Part imagePart = req.getPart("image");
        String name = req.getParameter("productName");
        String description = req.getParameter("description");
        int priceStr = (int) Double.parseDouble(req.getParameter("price"));
        int purchaseCost = (int) Double.parseDouble(req.getParameter("purchaseCost"));
        int categoryIdStr = 0;
        int stock = (int) Double.parseDouble(req.getParameter("stock"));
        int status = Integer.parseInt(req.getParameter("status"));
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

        // Đồng bộ với bảng ProductSize
        List<Size> currentSizeIds = pr.getSizesByProductId(productId);
        Set<Integer> updatedSizeSet = new HashSet<>(allSizeIds);

        // Xoá cái đã bị gỡ
        for (Size oldId : currentSizeIds) {
            if (!updatedSizeSet.contains(oldId.getSizeId())) {
                pr.delete(productId, oldId.getSizeId());
            }
        }

        // Thêm cái mới
        for (int newId : updatedSizeSet) {
            if (!pr.exists(productId, newId)) {
                pr.insert(productId, newId);
            }
        }

        if (req.getParameter("category") != null) {
            categoryIdStr = Integer.parseInt(req.getParameter("category"));
        }
        // If a new category is provided, add it to the database
        String newCategory = req.getParameter("newCategory");
        int categoryId = 0;
        if (newCategory != null && !newCategory.isEmpty()) {
            // If new category is provided, add it to the database
            categoryId = categoryService.addCategory(newCategory);
        }
        //Save image
        String imageUrl = "";
        if (imagePart != null && imagePart.getSize() > 0) {

            try {
                imageUrl = CloudinaryConfig.updloadImage(imagePart);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (imageUrl == null || imageUrl.isEmpty()) {
            // If no new image is uploaded, keep the existing image URL
            Product existingProduct = productService.getProductById(productId);
            if (existingProduct != null) {
                imageUrl = existingProduct.getImage();
            }
        }
        // Log the image URL for debugging
        System.out.println("Image URL: " + imageUrl);
        Product product = new Product().builder()
                .productId(productId)
                .productName(name)
                .description(description)
                .price(priceStr)
                .categoryId(categoryIdStr > 0 ? categoryIdStr : categoryId)
                .purchaseCost(purchaseCost)
                .image(imageUrl)
                .stock(stock)
                .status(status)
                .build();
        // Save the product to the database
        boolean isUpdated = productService.updateProduct(product);
        if (isUpdated) {
            // Redirect to the product list page with success message
            req.setAttribute("message", "Product updated successfully!");
            resp.sendRedirect(req.getContextPath() + "/product");
        } else {
            // Forward back to the add product page with error message
            req.setAttribute("error", "Failed to update product. Please try again.");
            req.getRequestDispatcher("/manager_pages/add_product.jsp").forward(req, resp);
        }
    }
}

package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import entites.Category;
import entites.Product;
import entites.Size;
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
import utils.ValidateProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@MultipartConfig
@WebServlet(name = "ProductController", urlPatterns = {"/product"})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private SizeService sizeService = new SizeService();
    private CategoryService categoryService = new CategoryService();
    private ProductSizeService pr = new ProductSizeService();
    private ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            getAllProducts(req, resp);
        } else {
            switch (action) {
                case "add-product":
                    addProducts(req, resp);
                    break;
                case "update-product":
                    updateProductDetails(req, resp);
                    break;
                case "enable-product":
                    enableProduct(req, resp);
                    break;
                case "disable-product":
                    disableProduct(req, resp);
                    break;
                default:
                    getAllProducts(req, resp);
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
            return;
        }
        switch (action) {
            case "add-product":
                handleAddProduct(req, resp);
                break;
            case "update-product":
                handleUpdateProductDetail(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
                break;
        }
    }

    //GET
    public void getAllProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    public void addProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("sizes", sizeService.getAllSizes());
        req.setAttribute("categories", categoryService.getAllCategories());
        req.getRequestDispatcher("/manager_pages/add_product.jsp").forward(req, resp);
    }

    public void updateProductDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    public void enableProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    public void disableProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdStr = req.getParameter("id");
        if (productIdStr != null && !productIdStr.isEmpty()) {
            int productId = Integer.parseInt(productIdStr);
            // Assuming ProductDAO has a method to disable a product by its ID
            boolean isDisabled = productService.disableProduct(productId);

            if (isDisabled) {
                resp.sendRedirect("product?message=Product disabled successfully");
            } else {
                resp.sendRedirect("product?error=Failed to disable product");
            }
        } else {
            resp.sendRedirect("product?error=Invalid product ID");
        }
    }

    //POST
    public void handleAddProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part imagePart = req.getPart("image");
        String name = req.getParameter("productName");
        System.out.println("Product Name: " + name);
        String description = req.getParameter("description");
        int priceStr = 0;
        if (req.getParameter("price") != null && !req.getParameter("price").isEmpty()) {
            priceStr = ValidateProduct.getIntegerValue(req.getParameter("price"));
            if (priceStr == -1) {
                System.out.println("Invalid price format");
            }
        }
        int categoryIdStr = 0;
        if (req.getParameter("category") != null) {
            categoryIdStr = ValidateProduct.getIntegerValue(req.getParameter("category"));
            if (categoryIdStr == -1) {
                System.out.println("Invalid category format");
            }
        }
        int purchaseCost = 0;
        if (req.getParameter("purchaseCost") != null && !req.getParameter("purchaseCost").isEmpty()) {
            // Validate purchase cost
            purchaseCost = ValidateProduct.getIntegerValue(req.getParameter("purchaseCost"));
            if (purchaseCost == -1) {
                req.setAttribute("error", "Invalid purchase cost format.");
                req.getRequestDispatcher("/manager_pages/add_product.jsp?err=").forward(req, resp);
                return;
            }
        }
        int stock = 0;
        if (req.getParameter("stock") != null && !req.getParameter("stock").isEmpty()) {
            // Validate stock
            stock = ValidateProduct.getIntegerValue(req.getParameter("stock"));
            if (stock == -1) {
                req.setAttribute("error", "Invalid stock format.");
                req.getRequestDispatcher("/manager_pages/add_product.jsp?err=").forward(req, resp);
                return;
            }
        }
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
        System.out.println("Add Product: " + isAdded);
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

    public void handleUpdateProductDetail(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
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

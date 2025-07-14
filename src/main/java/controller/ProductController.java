package controller;

import entites.Product;
import entites.Setting;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProductService;
import service.SettingService;
import proxy.CloudinaryConfig;
import utils.ValidateProduct;

import java.io.IOException;
import java.util.List;

@MultipartConfig
@WebServlet(name = "ProductController", urlPatterns = {"/product"})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private SettingService settingService = new SettingService();
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
        String search = req.getParameter("search");
        log.info("Page No: {}, Category: {}, Status: {}, Search: {}", pageNo, category, status, search);
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
            log.warn("Invalid number format in request parameters: {}", e.getMessage());
        }


        List<Product> products = productService.getAllProducts(categoryNum, statusNum, search);
        if (products == null) {
            products = List.of();
        }
        int pageCount = (int) Math.ceil(products.size() / 6.0);
        if (pageCount == 0) pageCount = 1;
        if (pageNum > pageCount) pageNum = 1;

        List<Setting> categories = settingService.getAllCategories();
        List<Product> productList = productService.getListProductPaginate(pageNum, categoryNum, statusNum, search);

        req.setAttribute("categoryCrr", categoryNum);
        req.setAttribute("statusCrr", statusNum);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", pageNum);
        req.setAttribute("pageCount", pageCount);
        req.setAttribute("search", search);
        req.setAttribute("productList", productList);
        req.getRequestDispatcher("/WEB-INF/view/manager_pages/product_list.jsp").forward(req, resp);
    }

    public void addProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", settingService.getAllCategories());
        req.getRequestDispatcher("/WEB-INF/view/manager_pages/add_product.jsp").forward(req, resp);
    }

    public void updateProductDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("id");
        if (productId != null && !productId.isEmpty()) {
            try {
                int id = Integer.parseInt(productId);
                // Thêm dòng này
                req.setAttribute("categories", settingService.getAllCategories());
                req.setAttribute("product", productService.getProductById(id));
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format");
                return;
            }
            req.getRequestDispatcher("/WEB-INF/view/manager_pages/update_product.jsp").forward(req, resp);
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
        log.info("Product Name: " + name);
        String description = req.getParameter("description");
        int priceStr = 0;
        if (req.getParameter("price") != null && !req.getParameter("price").isEmpty()) {
            priceStr = ValidateProduct.getIntegerValue(req.getParameter("price"));
            if (priceStr == -1) {
                log.info("Invalid price format");
            }
        }
        int categoryIdStr = 0;
        if (req.getParameter("category") != null) {
            categoryIdStr = ValidateProduct.getIntegerValue(req.getParameter("category"));
            if (categoryIdStr == -1) {
                log.error("Invalid category format");
            }
        }
        int purchaseCost = 0;
        if (req.getParameter("purchaseCost") != null && !req.getParameter("purchaseCost").isEmpty()) {
            // Validate purchase cost
            purchaseCost = ValidateProduct.getIntegerValue(req.getParameter("purchaseCost"));
            if (purchaseCost == -1) {
                req.getRequestDispatcher("product?action=add-product&err=Invalid purchase cost").forward(req, resp);
                return;
            }
        }
        int stock = 0;
        if (req.getParameter("stock") != null && !req.getParameter("stock").isEmpty()) {
            // Validate stock
            stock = ValidateProduct.getIntegerValue(req.getParameter("stock"));
            if (stock == -1) {
                req.getRequestDispatcher("product?action=add-product&err=Invalid stock").forward(req, resp);
                return;
            }
        }
        String newCategory = req.getParameter("newCategory");
        int categoryId = 0;

        if (newCategory != null && !newCategory.isEmpty()) {
            // If new category is provided, add it to the database
            categoryId = settingService.addCategory(newCategory);
        }
        String imageUrl = "";

        if (imagePart != null && imagePart.getSize() > 0) {

            try {
                imageUrl = CloudinaryConfig.updloadImage(imagePart);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        log.info("Image URL: " + imageUrl);
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
        log.info("Add Product: " + isAdded);
        if (isAdded != -1) {
            // Redirect to the product list page with success message
            req.setAttribute("message", "Product added successfully!");
            resp.sendRedirect(req.getContextPath() + "/product");
        } else {
            // Forward back to the add product page with error message
            req.setAttribute("error", "Failed to add product. Please try again.");
            req.getRequestDispatcher("/WEB-INF/view/manager_pages/add_product.jsp").forward(req, resp);
        }
    }

    public void handleUpdateProductDetail(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
        log.info(req.getParameter("productId"));
        int productId = Integer.parseInt(req.getParameter("productId"));
        Part imagePart = req.getPart("image");
        String name = req.getParameter("productName");
        String description = req.getParameter("description");
        int priceStr = (int) Double.parseDouble(req.getParameter("price"));
        int purchaseCost = (int) Double.parseDouble(req.getParameter("purchaseCost"));
        int categoryIdStr = 0;
        int stock = (int) Double.parseDouble(req.getParameter("stock"));
        int status = Integer.parseInt(req.getParameter("status"));
        if (req.getParameter("category") != null && !req.getParameter("category").isEmpty()) {
            categoryIdStr = Integer.parseInt(req.getParameter("category"));
        }
        // If a new category is provided, add it to the database
        String newCategory = req.getParameter("newCategory");
        int categoryId = 0;
        if (newCategory != null && !newCategory.isEmpty()) {
            // If new category is provided, add it to the database
            categoryId = settingService.addCategory(newCategory);
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
        log.info("Image URL: " + imageUrl);
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
            req.getRequestDispatcher("/WEB-INF/view/manager_pages/add_product.jsp").forward(req, resp);
        }
    }


}

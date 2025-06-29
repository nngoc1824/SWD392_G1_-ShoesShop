package service;

import dao.ProductDAO;
import entites.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts(int categoryId, int status) {
        // This method should interact with the ProductDAO to fetch all products
        List<Product> products = productDAO.getAllProducts(categoryId, status);
        if (products != null && !products.isEmpty()) {
            return products;
        }
        return null;
    }
    public Product getProductById(int productId) {
        // This method should interact with the ProductDAO to fetch a product by its ID
        return productDAO.getProductById(productId);
    }
    public List<Product> getProductPagination(int pageNo, int categoryId, int status) {
        // This method should interact with the ProductDAO to fetch products with pagination
        return productDAO.getListProductPaginate(pageNo, categoryId, status);
    }
    public int addProduct(Product product) {
        // This method should interact with the ProductDAO to add a new product
        return productDAO.addProduct(product);
    }
    public boolean updateProduct(Product product) {
        // This method should interact with the ProductDAO to update an existing product
        return productDAO.updateProductDetail(product);
    }
}

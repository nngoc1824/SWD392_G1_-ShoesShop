package service;

import dao.ProductDAO;
import entites.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts(int categoryId, int status, String searchQuery) {
        // This method should interact with the ProductDAO to fetch all products
        List<Product> products = productDAO.getAllProducts(categoryId, status, searchQuery);
        if (products != null && !products.isEmpty()) {
            return products;
        }
        return null;
    }
    public Product getProductById(int productId) {
        // This method should interact with the ProductDAO to fetch a product by its ID
        return productDAO.getProductById(productId);
    }
    public List<Product> getProductPagination(int pageNo, int categoryId, int status, String searchQuery) {
        // This method should interact with the ProductDAO to fetch products with pagination
        return productDAO.getListProductPaginate(pageNo, categoryId, status,searchQuery );
    }
    public int addProduct(Product product) {

        // This method should interact with the ProductDAO to add a new product
        return productDAO.addProduct(product);
    }
    public boolean updateProduct(Product product) {
        // This method should interact with the ProductDAO to update an existing product
        return productDAO.updateProductDetail(product);
    }
    public boolean disableProduct(int productId) {
        // This method should interact with the ProductDAO to delete a product
        return productDAO.disableProduct(productId);
    }

    public boolean enableProduct(int productId) {
        // This method should interact with the ProductDAO to enable a product
        return productDAO.enableProduct(productId);
    }

    public List<Product> getListProductPaginate(int pageNum, int categoryNum, int statusNum, String searchQuery) {
        // This method should interact with the ProductDAO to fetch products with pagination
        return productDAO.getListProductPaginate(pageNum, categoryNum, statusNum,searchQuery);
    }

    public List<Product> searchProducts(String searchQuery) {
        // This method should interact with the ProductDAO to search for products
        return productDAO.searchProducts(searchQuery);
    }
}

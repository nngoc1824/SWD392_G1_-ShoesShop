package service;

import dao.ProductSizeDAO;
import dao.SizeDAO;
import entites.Size;

import java.util.List;

public class ProductSizeService {
    private ProductSizeDAO productSizeDAO = new ProductSizeDAO();
    private SizeDAO sizeDAO = new SizeDAO();

    public boolean addProductSize(int productId, int sizeId) {
        return productSizeDAO.addProductSize(productId, sizeId);
    }

    public boolean deleteProductSize(int productId, int sizeId) {
        return productSizeDAO.deleteProductSize(productId, sizeId);
    }

    public List<Size> getSizesByProductId(int productId) {
        return productSizeDAO.getSizesByProductId(productId);
    }

    public List<Size> getAllSizes() {
        return sizeDAO.getAllSizes();
    }

    public void insert(int productId, int sizeId) {
        productSizeDAO.addProductSize(productId, sizeId);
    }
    public boolean exists(int productId, int sizeId) {
        return productSizeDAO.exists(productId, sizeId);
    }

    public void delete(int productId, int sizeId) {
        productSizeDAO.deleteProductSize(productId, sizeId);
    }
}

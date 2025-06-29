package service;

import dao.CategoryDAO;
import entites.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO = new CategoryDAO();

    public int addCategory(String categoryName) {
        // This method should interact with the CategoryDAO to add a new category
        int isAdded = categoryDAO.addCategory(categoryName) ;
        return isAdded;
    }

    public List<Category> getAllCategories() {
        // This method should interact with the CategoryDAO to fetch all categories
        List<Category> categories = categoryDAO.getAllCategories();
        if (categories != null && !categories.isEmpty()) {
            return categories;
        }
        return null;
    }
}

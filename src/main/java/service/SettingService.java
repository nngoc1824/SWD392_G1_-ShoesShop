package service;

import dao.SettingDAO;
import entites.Setting;

import java.util.List;

//TODO: fix get all service 

public class SettingService {
    private SettingDAO settingDAO = new SettingDAO();

    public List<Setting> getAllCategories() {
        List<Setting> settingList = settingDAO.getAllCategories();
        return settingList;
    }

    public int addCategory(String newCategory) {
        if (newCategory == null || newCategory.isEmpty()) {
            return -1; // Indicate an error for invalid input
        }
        int categoryId = settingDAO.addCategory(newCategory);
        return categoryId; // Placeholder return value, should be replaced with actual implementation
    }

    public static void main(String[] args) {
        SettingService settingService = new SettingService();
        List<Setting> categories = settingService.getAllCategories();
        System.out.println("List of Categories:" + categories.size());
        if (categories != null) {
            for (Setting category : categories) {
                System.out.println("Category ID: " + category.getSettingId() + ", Name: " + category.getSettingName());
            }
        } else {
            System.out.println("No categories found.");
        }
    }
}

package dao;

import entites.Setting;
import utils.DBContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SettingDAO extends DBContext {
    public List<Setting> getAllCategories() {
        String sql = "SELECT * FROM Setting where setting_type = 'category'";
        List<Setting> settingList = null;
        try (Connection connection = getConnection()) {
            settingList = new ArrayList<>();
            var preparedStatement = connection.prepareStatement(sql);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Setting setting = getSettingFromRs(resultSet);
                settingList.add(setting);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return settingList;
    }

    public Setting getSettingFromRs(ResultSet rs) {
        try {
            return Setting.builder()
                    .settingId(rs.getInt("setting_id"))
                    .settingName(rs.getString("setting_name"))
                    .settingType(rs.getString("setting_type"))
                    .settingValue(rs.getString("setting_value"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int addCategory(String newCategory) {
        String sql = "INSERT INTO Setting (setting_name, setting_type, setting_value) VALUES (?, 'category', ?)";
        try (Connection connection = getConnection()) {
            var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newCategory);
            preparedStatement.setString(2, newCategory); // Assuming setting_value is the same as setting_name
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the ID of the newly added category
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Indicate an error occurred
        }
        return -1; // Return the ID of the newly added category or an error code
    }

    public static void main(String[] args) {
        SettingDAO settingDAO = new SettingDAO();
        List<Setting> categories = settingDAO.getAllCategories();
        System.out.println("List of Categories: " + categories.size());
        if (categories != null) {
            for (Setting category : categories) {
                System.out.println("Category ID: " + category.getSettingId() + ", Name: " + category.getSettingName());
            }
        } else {
            System.out.println("No categories found.");
        }
    }

}

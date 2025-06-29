package dal;

import entites.User;
import utils.DBContext;

import java.sql.*;

public class UserDAO {

    private static Connection getConnection() throws Exception {
        return new DBContext().getConnection();
    }

    // LOGIN
    public static User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return User.builder()
                        .userId(rs.getInt("user_id"))
                        .userName(rs.getString("user_name"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .fullName(rs.getString("full_name"))
                        .phone(rs.getString("phone"))
                        .image(rs.getString("image"))
                        .status(rs.getInt("status"))
                        .addressId(rs.getInt("address_id"))
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // CHECK EMAIL EXISTENCE
    public static boolean checkEmailExists(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    // REGISTER
    public static boolean register(String userName, String fullName, String email, String phone,
                                   String password, String image, int addressId) {

        String sql = "INSERT INTO users (user_name, full_name, email, phone, password, image, status, address_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userName);
            ps.setString(2, fullName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, password);
            ps.setString(6, image);
            ps.setInt(7, 1); // 1: active
            ps.setInt(8, addressId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // RESET PASSWORD
    public static boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // FIND USER BY EMAIL
    public static User findUserFromEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return User.builder()
                        .userId(rs.getInt("user_id"))
                        .userName(rs.getString("user_name"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .fullName(rs.getString("full_name"))
                        .phone(rs.getString("phone"))
                        .image(rs.getString("image"))
                        .status(rs.getInt("status"))
                        .addressId(rs.getInt("address_id"))
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

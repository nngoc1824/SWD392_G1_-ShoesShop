package dao;

import entites.User;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ Đăng nhập: kiểm tra username + password
    public User login(String username, String password) {
        String sql = "SELECT * FROM User WHERE user_name = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
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
                        .googleId(rs.getString("google_id"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Đăng ký: thêm user mới
    public boolean register(User user) {
        String sql = "INSERT INTO User(user_name, password, email, full_name, phone, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhone());
            ps.setInt(6, user.getStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Kiểm tra username tồn tại
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT 1 FROM User WHERE user_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE User SET full_name = ?, email = ?, phone = ?, image = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getImage());
            ps.setInt(5, user.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<String> getRole(int userID){
        String sql = "SELECT r.setting_name FROM UserRole ur JOIN Setting r ON ur.role_id = r.setting_id WHERE ur.user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            List<String> roles = new ArrayList<>();
            while (rs.next()) {
                roles.add(rs.getString("setting_name"));
            }
            return roles;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return User.builder()
                        .userId(rs.getInt("user_id"))
                        .userName(rs.getString("user_name"))
                        .email(rs.getString("email"))
                        .password(rs.getString("password"))
                        .fullName(rs.getString("full_name"))
                        .phone(rs.getString("phone"))
                        .image(rs.getString("image"))
                        .googleId(rs.getString("google_id"))
                        .status(rs.getInt("status"))
                        .build();
            }
        }
        return null;
    }

    public static void insertGoogleUser(User user, Connection conn) {
        String sql = "INSERT INTO User (user_name, password, email, full_name, phone, image, google_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String generatedUserName = user.getEmail();
            ps.setString(1, generatedUserName);
            ps.setString(2, "google");
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhone() == null ? "" : user.getPhone());
            ps.setString(6, user.getImage() == null ? "" : user.getImage());
            ps.setString(7, user.getGoogleId());
            ps.setInt(8, 1);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean activateUserByEmail(String email, Connection conn) {
        String sql = "UPDATE User SET status = 1 WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateGoogleId(User user, Connection conn) {
        String sql = "UPDATE User SET google_id = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getGoogleId());
            ps.setInt(2, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateUserPassword(int userId, String newPassword) {
        String sql = "UPDATE User SET password = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User findByGoogleId(String googleId, Connection conn) {
        String sql = "SELECT * FROM User WHERE google_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, googleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return User.builder()
                        .userId(rs.getInt("user_id"))
                        .userName(rs.getString("user_name"))
                        .email(rs.getString("email"))
                        .fullName(rs.getString("full_name"))
                        .image(rs.getString("image"))
                        .googleId(rs.getString("google_id"))
                        .status(rs.getInt("status"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isManager(int userId) {
        String sql = "SELECT role_id FROM UserRole WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int roleId = rs.getInt("role_id");
                if (roleId == 2) { // giả sử 2 là Manager
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/OSS?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "1234";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            UserDAO userDAO = new UserDAO(conn);

            int userIdToCheck = 3; // Nhập ID user cần kiểm tra ở đây

            boolean isManager = userDAO.isManager(userIdToCheck);

            if (isManager) {
                System.out.println("✅ User ID " + userIdToCheck + " là Manager.");
            } else {
                System.out.println("❌ User ID " + userIdToCheck + " KHÔNG phải Manager.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}

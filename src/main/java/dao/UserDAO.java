package dao;

import entites.User;
import utils.DBContext;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ Đăng nhập: kiểm tra username + password
    public User login(String username, String password) {
        String sql = "SELECT * FROM User WHERE userName = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return User.builder()
                        .userId(rs.getInt("userId"))
                        .userName(rs.getString("userName"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .fullName(rs.getString("fullName"))
                        .phone(rs.getString("phone"))
                        .image(rs.getString("image"))
                        .status(rs.getInt("status"))
                        .addressId(rs.getInt("addressId"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Đăng ký: thêm user mới
    public boolean register(User user) {
        String sql = "INSERT INTO User(userName, password, email, fullName, phone, status) " +
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

    // 🧩 Có thể thêm phương thức kiểm tra trùng username nếu cần
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT 1 FROM User WHERE userName = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // tồn tại username
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE User SET fullName = ?, email = ?, phone = ?, image =? WHERE userId = ?";
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

    public class SignWithGoogleDAO {
        private Connection conn;

        public SignWithGoogleDAO(Connection conn) {
            this.conn = conn;
        }
    }

        public User getUserByEmail(String email) throws SQLException {
            String sql = "SELECT * FROM User WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return User.builder()
                            .userId(rs.getInt("userId"))
                            .userName(rs.getString("userName"))
                            .email(rs.getString("email"))
                            .fullName(rs.getString("fullName"))
                            .image(rs.getString("image"))
                            .build();
                }
            }
            return null;
        }

    public static User findByGoogleId(String googleId) {
        String sql = "SELECT * FROM User WHERE googleId = ?";
        try (
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, googleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("fullName"));
                user.setGoogleId(rs.getString("googleId"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertGoogleUser(User user) {
        String sql = "INSERT INTO User (email, password, phone, fullName, googleId) " +
                "VALUES (?, NULL, NULL, NULL, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());         // email
            ps.setString(2, user.getFullName());      // fullName
            ps.setString(3, user.getGoogleId());      // googleId
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setFullName(rs.getString("fullName"));
                user.setGoogleId(rs.getString("googleId"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void updateGoogleId(User user) {
        String sql = "UPDATE User SET googleId = ? WHERE userId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getGoogleId());
            ps.setInt(2, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateUserPassword(int userId, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE userId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}



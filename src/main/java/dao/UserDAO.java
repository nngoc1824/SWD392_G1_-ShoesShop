package dao;

import entites.User;
import utils.DBContext;
import java.sql.*;

public class UserDAO {
    private Connection conn;

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
        String sql = "UPDATE User SET fullName = ?, email = ?, phone = ? WHERE userId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setInt(4, user.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            // 1. Kết nối DB
            DBContext dbContext = new DBContext();
            Connection conn = dbContext.getConnection();
            System.out.println("✅ Kết nối thành công");

            // 2. Tạo User giả lập để test
            User newUser = User.builder()
                    .userName("testuser")
                    .password("123456")
                    .email("test@example.com")
                    .fullName("Nguyễn Văn A")
                    .phone("0123456789")
                    .status(1)
                    .addressId(1) // đảm bảo addressId = 1 tồn tại
                    .build();

            // 3. Gọi DAO và thực hiện đăng ký
            UserDAO userDAO = new UserDAO(conn);
            boolean result = userDAO.register(newUser);

            // 4. In kết quả
            if (result) {
                System.out.println("✅ Đăng ký thành công!");
            } else {
                System.out.println("❌ Đăng ký thất bại.");
            }

            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Lỗi kết nối hoặc SQL:");
            e.printStackTrace();
        }
    }

}

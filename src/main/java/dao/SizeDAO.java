package dao;

import entites.Size;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SizeDAO extends DBContext {
    public List<Size> getAllSizes() {
        String sql = "SELECT * FROM Size";
        List<Size> sizes = null;

        try (Connection conn = getConnection()) {
            var ps = conn.prepareStatement(sql);
            var rs = ps.executeQuery();
            sizes = new java.util.ArrayList<>();
            while (rs.next()) {
                Size size = new Size();
                size.setSizeId(rs.getInt("sizeId"));
                size.setSizeNumber(rs.getString("sizeNumber"));
                sizes.add(size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sizes;
    }public int insertAndGetId(String sizeNumber) {
        String sql = "INSERT INTO Size(sizeNumber) VALUES(?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sizeNumber);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Lấy sizeId mới
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}

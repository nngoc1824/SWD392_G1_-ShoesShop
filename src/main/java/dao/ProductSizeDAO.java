package dao;

import entites.Size;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductSizeDAO extends DBContext {
    public boolean addProductSize(int productId, int sizeId) {
        String sql = "INSERT INTO ProductSize (productId, sizeId) VALUES (?, ?)";
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, sizeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProductSize(int productId, int sizeId) {
        String sql = "DELETE FROM ProductSize WHERE productId = ? AND sizeId = ?";
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, sizeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Size> getSizesByProductId(int productId) {
        String sql = "SELECT s.sizeId, s.sizeNumber " +
                "FROM ProductSize ps " +
                "JOIN Size s ON ps.sizeId = s.sizeId " +
                "WHERE ps.productId = ?";
        List<Size> sizes = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Size size = new Size();
                    size.setSizeId(rs.getInt("sizeId"));
                    size.setSizeNumber(rs.getString("sizeNumber"));
                    sizes.add(size);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sizes;
    }
    public boolean exists(int productId, int sizeId) {
        String sql = "SELECT 1 FROM ProductSize WHERE productId = ? AND sizeId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, sizeId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true nếu tồn tại
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void delete(int productId, int sizeId) {
        String sql = "DELETE FROM ProductSize WHERE productId = ? AND sizeId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, sizeId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

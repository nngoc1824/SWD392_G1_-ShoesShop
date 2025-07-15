package utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    /**
     * Kết nối tới database MySQL
     *
     * @return Connection đối tượng kết nối
     * @throws ClassNotFoundException nếu không tìm thấy driver MySQL
     * @throws SQLException           nếu có lỗi khi kết nối tới database
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();
        String db_name = dotenv.get("DB_NAME");
        String user = dotenv.get("MYSQL_USER");
        String password = dotenv.get("MYSQL_PASSWORD");
        String url = "jdbc:mysql://localhost:3306/" + db_name + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            System.out.println("Kết nối thành công ");
            System.out.println("Đang kết nối tới database: " + conn.getCatalog());
        } catch (Exception e) {
            System.out.println("Lỗi kết nối database:");
            e.printStackTrace();
        }
    }
}
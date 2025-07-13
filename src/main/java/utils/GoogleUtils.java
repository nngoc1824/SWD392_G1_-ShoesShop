package utils;

import entites.User;
import dao.UserDAO;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

public class GoogleUtils {
    private static final String CLIENT_ID = "841040454506-pttbflftv7fu6qsdqpn7ju434hpag7be.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/SWD392_ShoesShop_war_exploded/login-google";

    public static User handleGoogleLogin(String code) {
        try (Connection conn = new DBContext().getConnection()) {
            String token = getAccessToken(code);
            if (token == null) return null;

            User googleUser = getUserInfo(token);
            System.out.println("🔍 Google user: " + googleUser);

            if (googleUser == null || googleUser.getGoogleId() == null) return null;

            UserDAO userDAO = new UserDAO(conn);

            // Tìm theo google_id
            User existingUser = UserDAO.findByGoogleId(googleUser.getGoogleId(), conn);

            if (existingUser == null) {
                // Nếu đã có user theo email -> update googleId
                User emailUser = userDAO.getUserByEmail(googleUser.getEmail());
                if (emailUser != null) {
                    emailUser.setGoogleId(googleUser.getGoogleId());
                    UserDAO.updateGoogleId(emailUser, conn);
                    return emailUser;
                } else {
                    // Tạo mới user google
                    UserDAO.insertGoogleUser(googleUser, conn);
                    return UserDAO.findByGoogleId(googleUser.getGoogleId(), conn);
                }
            }

            return existingUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getAccessToken(String code) throws IOException {
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String params = "code=" + code
                + "&client_id=" + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET
                + "&redirect_uri=" + REDIRECT_URI
                + "&grant_type=authorization_code";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes());
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JSONObject json = new JSONObject(response.toString());
        return json.optString("access_token", null);
    }

    private static User getUserInfo(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JSONObject json = new JSONObject(response.toString());

        User user = new User();
        user.setGoogleId(json.optString("id", null));
        user.setEmail(json.optString("email", null));
        user.setFullName(json.optString("name", null));
        user.setImage(json.optString("picture", null)); // nếu muốn lưu avatar
        return user;
    }
}

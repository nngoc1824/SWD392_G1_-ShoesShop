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

public class GoogleUtils {
    private static final String CLIENT_ID = "841040454506-pttbflftv7fu6qsdqpn7ju434hpag7be.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/SWD392_ShoesShop_war_exploded/login-google";

    public static User handleGoogleLogin(String code) {
        try {
            String token = getAccessToken(code);
            if (token == null) return null;

            User googleUser = getUserInfo(token);
            System.out.println(googleUser);
            if (googleUser == null || googleUser.getGoogleId() == null) return null;
            UserDAO userDAO = new UserDAO(new DBContext().getConnection());

            User existingUser = userDAO.findByGoogleId(googleUser.getGoogleId());

            if (existingUser == null) {
                User emailUser = userDAO.findByEmail(googleUser.getEmail());
                if (emailUser != null) {
                    emailUser.setGoogleId(googleUser.getGoogleId());
                    UserDAO.updateGoogleId(emailUser);
                    return emailUser;
                } else {

                    UserDAO.insertGoogleUser(googleUser);
                    return UserDAO.findByGoogleId(googleUser.getGoogleId());
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
        return user;
    }


}

package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/getDistricts")
public class DistrictServlet extends HttpServlet {
    private static final String TOKEN = "bdedfc18-5520-11f0-9b81-222185cb68c8";
    private static final String API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String provinceId = request.getParameter("provinceId");

        if (provinceId == null || provinceId.isEmpty()) {
            response.setStatus(400);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing provinceId\"}");
            return;
        }

        try {
            // Gửi request đến GHN
            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Token", TOKEN);
            conn.setDoOutput(true);

            String jsonBody = "{\"province_id\":" + provinceId + "}";
            conn.getOutputStream().write(jsonBody.getBytes());

            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            // Trả JSON về client
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(sb.toString());

        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi server
            response.setStatus(500);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Internal server error while getting districts.\"}");
        }
    }
}

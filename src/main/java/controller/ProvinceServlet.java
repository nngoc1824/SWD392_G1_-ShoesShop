package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/getProvinces")
public class ProvinceServlet extends HttpServlet {
    private static final String TOKEN = "d6e3dccb-6289-11ea-8b85-c60e4edfe802";
    private static final String API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Token", TOKEN);
            conn.setDoOutput(true);
            conn.getOutputStream().write("{}".getBytes());

            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(status); // truyền mã lỗi nếu có
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra log Tomcat
            response.setStatus(500);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"GHN API failed or token invalid.\"}");
        }
    }
}

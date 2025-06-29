package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/getDistricts")
public class DistrictServlet extends HttpServlet {
    private static final String TOKEN = "bdedfc18-5520-11f0-9b81-222185cb68c8";
    private static final String API_URL = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String provinceId = request.getParameter("provinceId");
        if (provinceId == null) return;

        HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Token", TOKEN);
        conn.setDoOutput(true);
        conn.getOutputStream().write(("{\"province_id\":" + provinceId + "}").getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(sb.toString());
    }
}

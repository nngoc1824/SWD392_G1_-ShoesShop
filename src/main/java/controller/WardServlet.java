package controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/getWards")
public class WardServlet extends HttpServlet {
    private static final String TOKEN = "bdedfc18-5520-11f0-9b81-222185cb68c8";
    private static final String API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String districtId = request.getParameter("districtId");
        if (districtId == null) return;

        HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Token", TOKEN);
        conn.setDoOutput(true);
        conn.getOutputStream().write(("{\"district_id\":" + districtId + "}").getBytes());

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

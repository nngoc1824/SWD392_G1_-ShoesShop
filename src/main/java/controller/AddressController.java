package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/address")
public class AddressController extends HttpServlet {
    private static final String TOKEN = "796859fa-5fca-11f0-9b80-96385126568f";
    private static final String GHN_PROVINCE_API = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
    private static final String GHN_DISTRICT_API = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
    private static final String GHN_WARD_API = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        String apiUrl;
        String body = "{}";

        switch (action) {
            case "province":
                apiUrl = GHN_PROVINCE_API;
                break;

            case "district":
                String provinceId = request.getParameter("provinceId");
                if (provinceId == null || provinceId.isEmpty()) {
                    response.setStatus(400);
                    response.getWriter().write("{\"error\": \"Missing provinceId\"}");
                    return;
                }
                apiUrl = GHN_DISTRICT_API;
                body = "{\"province_id\":" + provinceId + "}";
                break;

            case "ward":
                String districtId = request.getParameter("districtId");
                if (districtId == null || districtId.isEmpty()) {
                    response.setStatus(400);
                    response.getWriter().write("{\"error\": \"Missing districtId\"}");
                    return;
                }
                apiUrl = GHN_WARD_API;
                body = "{\"district_id\":" + districtId + "}";
                break;

            default:
                response.setStatus(400);
                response.getWriter().write("{\"error\": \"Invalid action\"}");
                return;
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Token", TOKEN);
            conn.setDoOutput(true);
            conn.getOutputStream().write(body.getBytes());

            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Internal server error in AddressController\"}");
        }
    }
}

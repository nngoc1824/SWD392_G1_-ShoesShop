package controller;

import dao.ProductDAO;
import entites.CartItem;
import entites.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@WebServlet("/confirmOrder")
public class ConfirmOrderController extends HttpServlet {

    private ProductDAO productDAO;

    private static final String GHN_API = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
    private static final String TOKEN = "796859fa-5fca-11f0-9b80-96385126568";
    private static final String SHOP_ID = "4854101";
    private static final int FROM_DISTRICT_ID = 1451; // Mặc định: ví dụ HCM

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy giỏ hàng từ cookie
        Map<Integer, Integer> cartMap = getCartFromCookie(request);
        List<CartItem> cartItems = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productDAO.getProductById(productId);
            if (product != null) {
                CartItem item = new CartItem();
                item.setProductId(productId);
                item.setProductId(product.getProductId()); // set actual product object
                item.setQuantity(quantity);
                item.setPrice(product.getPrice() * quantity);
                cartItems.add(item);
            }
        }

        request.setAttribute("cart", cartItems);
        request.getRequestDispatcher("confirmOrder.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String wardCode = request.getParameter("ward");
        String districtStr = request.getParameter("district");

        int shippingFee = 0;
        if (wardCode != null && districtStr != null) {
            try {
                int districtId = Integer.parseInt(districtStr);
                shippingFee = calculateShippingFee(districtId, wardCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Đọc giỏ hàng từ cookie
        Map<Integer, Integer> cartMap = getCartFromCookie(request);
        List<CartItem> cartItems = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            if (quantity <= 0) continue; // Bỏ qua nếu số lượng không hợp lệ
            Product product = productDAO.getProductById(productId);
            if (product != null) {
                CartItem item = new CartItem();
                item.setProductId(productId);
                item.setCartItemId(productId); // set actual product object
                item.setQuantity(quantity);
                item.setPrice(product.getPrice() * quantity);
                cartItems.add(item);
            }
        }

        request.setAttribute("cart", cartItems);
        request.setAttribute("shippingFee", shippingFee);
        request.getRequestDispatcher("confirmOrder.jsp").forward(request, response);
    }

    private int calculateShippingFee(int toDistrictId, String toWardCode) {
        try {
            URL url = new URL(GHN_API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Token", TOKEN);
            conn.setRequestProperty("ShopId", SHOP_ID);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("from_district_id", FROM_DISTRICT_ID);
            json.put("service_type_id", 2);
            json.put("to_district_id", toDistrictId);
            json.put("to_ward_code", toWardCode);
            json.put("height", 10);
            json.put("length", 20);
            json.put("weight", 500);
            json.put("width", 15);
            json.put("insurance_value", 0);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.toString().getBytes("utf-8"));
            }

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject resJson = new JSONObject(result.toString());
                return resJson.getJSONObject("data").getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Map<Integer, Integer> getCartFromCookie(HttpServletRequest request) {
        Map<Integer, Integer> cartMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return cartMap;

        for (Cookie c : cookies) {
            if ("cart".equals(c.getName())) {
                try {
                    String decoded = URLDecoder.decode(c.getValue(), "UTF-8");
                    String[] items = decoded.split(",");
                    for (String item : items) {
                        if (item.isEmpty()) continue;
                        String[] parts = item.split(":");
                        int pid = Integer.parseInt(parts[0]);
                        int qty = Integer.parseInt(parts[1]);
                        cartMap.put(pid, qty);
                    }
                } catch (Exception ignored) {}
            }
        }
        return cartMap;
    }
}

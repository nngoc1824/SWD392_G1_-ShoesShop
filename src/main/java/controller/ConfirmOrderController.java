package controller;

import dao.ProductDAO;
import entites.CartItem;
import entites.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.GHNProxy;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@WebServlet("/confirmOrder")
public class ConfirmOrderController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderController.class);
    private ProductDAO productDAO;
    private GHNProxy ghnProxy;

    @Override
    public void init() {
        productDAO = new ProductDAO();
        ghnProxy = new GHNProxy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        try {
            if ("province".equals(action)) {
                handleProvinces(response);
                return;
            } else if ("district".equals(action)) {
                handleDistricts(request, response);
                return;
            } else if ("ward".equals(action)) {
                handleWards(request, response);
                return;
            } else if ("shippingFee".equals(action)) {
                handleShippingFee(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return;
        }

        loadPageData(request);
        request.getRequestDispatcher("/WEB-INF/confirmOrder.jsp").forward(request, response);
        request.getRequestDispatcher("confirmOrder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String wardCode = request.getParameter("ward");
        int districtStr = Integer.parseInt(request.getParameter("district"));
        log.info("Ward Code: {}, District ID: {}", wardCode, districtStr);
        int shippingFee = 0;
        try {
            shippingFee = ghnProxy.calculateShippingFee(districtStr, wardCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        loadPageData(request);
        request.setAttribute("shippingFee", shippingFee);

        request.getRequestDispatcher("/WEB-INF/confirmOrder.jsp").forward(request, response);
    }

    private void handleProvinces(HttpServletResponse response) throws Exception {
        String provinces = ghnProxy.getProvinces();
        response.setContentType("application/json");
        response.getWriter().write(provinces);
    }

    private void handleDistricts(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String provinceId = request.getParameter("provinceId");
        String districts = ghnProxy.getDistricts(Integer.parseInt(provinceId));
        response.setContentType("application/json");
        response.getWriter().write(districts);
    }

    private void handleWards(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String districtId = request.getParameter("districtId");
        String wards = ghnProxy.getWards(Integer.parseInt(districtId));
        response.setContentType("application/json");
        response.getWriter().write(wards);
    }

    private void handleShippingFee(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int districtId = Integer.parseInt(request.getParameter("districtId"));
        String wardCode =request.getParameter("wardCode");
        log.info("Calculating shipping fee for district ID: {}, ward code: {}", districtId, wardCode);
        int shippingFee = ghnProxy.calculateShippingFee(districtId, wardCode);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"fee\": " + shippingFee + "}");
    }

    private void loadPageData(HttpServletRequest request) {
        List<CartItem> cartItems = new ArrayList<>();
        Map<Integer, Integer> cartMap = getCartFromCookie(request);

        for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();
            if (quantity <= 0) continue;

            Product product = productDAO.getProductById(productId);
            if (product != null) {
                CartItem item = new CartItem();
                item.setProductId(productId);
                item.setQuantity(quantity);
                item.setPrice(product.getPrice() * quantity);
                cartItems.add(item);
            }
        }

        request.setAttribute("cart", cartItems);
        HttpSession session = request.getSession(true);
        session.setAttribute("cartItem", cartItems);

        try {
            String provinces = ghnProxy.getProvinces();
            request.setAttribute("provincesJson", provinces);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        if (qty > 0) {
                            cartMap.put(pid, qty);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return cartMap;
    }
}

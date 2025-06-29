package controller;

import dao.ProductDAO;
import entites.CartItem;
import entites.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("remove".equals(action)) {
            handleRemove(request, response);
        } else {
            showCart(request, response);
        }
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<Integer, Integer> cartMap = getCartFromCookie(request);

        List<CartItem> cartItems = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = productDAO.getProductById(productId);
            if (product != null) {
                CartItem item = new CartItem();
                item.setProductId(productId);
                item.setProductId(product.getProductId());
                item.setQuantity(quantity);
                item.setPrice(product.getPrice() * quantity);
                cartItems.add(item);
            }
        }

        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    // Thêm mới hoặc cập nhật số lượng
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int productId = Integer.parseInt(request.getParameter("productId"));

        Map<Integer, Integer> cart = getCartFromCookie(request);

        if ("update".equals(action)) {
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            if (quantity <= 0) cart.remove(productId);
            else cart.put(productId, quantity);
        } else {
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        }

        saveCartToCookie(response, cart);
        response.sendRedirect("cart");
    }

    private void handleRemove(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));

        Map<Integer, Integer> cart = getCartFromCookie(request);
        cart.remove(productId);

        saveCartToCookie(response, cart);
        response.sendRedirect("cart");
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

    private void saveCartToCookie(HttpServletResponse response, Map<Integer, Integer> cartMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : cartMap.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        String encoded = URLEncoder.encode(sb.toString(), java.nio.charset.StandardCharsets.UTF_8);

        Cookie cartCookie = new Cookie("cart", encoded);
        cartCookie.setMaxAge(60 * 60 * 24); // 1 ngày
        cartCookie.setPath("/");
        response.addCookie(cartCookie);
    }
}

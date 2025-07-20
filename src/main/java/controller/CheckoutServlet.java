package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ProductDAO;
import entites.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.OrderItemService;
import service.OrderService;
import utils.DBContext;
import proxy.PayOSInitializer;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout/create", "/checkout/confirm-webhook", "/checkout/confirm"})
public class CheckoutServlet extends HttpServlet {
    private final PayOS payOS = PayOSInitializer.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/checkout/create" -> handleCreatePayment(req, resp);
            case "/checkout/confirm-webhook" -> handleWebhook(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String orderCode = req.getParameter("orderCode");
            if (orderCode == null) {
                resp.sendRedirect("/checkout/error.jsp");
                return;
            }

            List<CartItem> cartItems = getCartItemsFromPendingCartCookie(req);
            String phone = getCookieValue(req, "pendingPhone");
            String address = getCookieValue(req, "pendingAddress");

            if (cartItems.isEmpty() || phone == null || address == null) {
                resp.sendRedirect("/checkout/error.jsp");
                return;
            }
            long orderIdLong = Long.parseLong(orderCode); // AN TOÀN với số lớn

            double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();


            System.out.println(getUserIdFromSessionOrRequest(req));

            Order order = Order.builder()
                    .orderId((int) orderIdLong)
                    .phone(URLDecoder.decode(phone, "UTF-8"))
                    .shipAddress(URLDecoder.decode(address, "UTF-8"))
                    .totalPrice(total)
                    .paymentStatus("Paid")
                    .status(true)
                    .orderDate(new Date())
                    .userId(getUserIdFromSessionOrRequest(req))
                    .build();

            try (Connection conn = new DBContext().getConnection()) {
                new OrderService(conn).save(order);
                OrderItemService orderItemService = new OrderItemService(conn);
                for (CartItem item : cartItems) {
                    orderItemService.save(OrderItem.builder()
                            .orderId((int) orderIdLong)
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build());
                }
            }

            System.out.println("✅ Order & Items đã lưu (returnUrl)");

            req.getRequestDispatcher("/checkout/success.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/checkout/error.jsp");
        }
    }

    private void handleCreatePayment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CartItem> cartItems = getCartItemsFromCookie(req);

            if (cartItems.isEmpty()) {
                resp.sendRedirect("/cart.jsp");
                return;
            }

            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String phone = req.getParameter("phone");
            double shippingFee = Double.parseDouble(req.getParameter("shippingFee"));

            double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum() + shippingFee;

            String address = req.getParameter("address");
            String province = req.getParameter("province");
            String district = req.getParameter("district");
            String ward = req.getParameter("ward");
            String fullAddress = address + ", " + ward + ", " + district + ", " + province;

            long orderCode = generateOrderCode();
            String description = "Đơn hàng " + orderCode;

            // 👉 Lưu cartItems vào cookie (Base64)
            String cartJson = mapper.writeValueAsString(cartItems);
            String encodedCart = Base64.getEncoder().encodeToString(cartJson.getBytes());
            Cookie cartCookie = new Cookie("pendingCart", encodedCart);
            cartCookie.setPath("/");
            cartCookie.setMaxAge(10 * 60);
            resp.addCookie(cartCookie);

            // 👉 Lưu shipping info vào cookie
            Cookie phoneCookie = new Cookie("pendingPhone", URLEncoder.encode(phone, "UTF-8"));
            Cookie addressCookie = new Cookie("pendingAddress", URLEncoder.encode(fullAddress, "UTF-8"));
            phoneCookie.setPath("/");
            addressCookie.setPath("/");
            phoneCookie.setMaxAge(10 * 60);
            addressCookie.setMaxAge(10 * 60);
            resp.addCookie(phoneCookie);
            resp.addCookie(addressCookie);

            // 👉 Tạo payment link
            ItemData item = ItemData.builder()
                    .name(description)
                    .quantity(1)
                    .price((int) total)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount((int) total)
                    .description(description)
                    .returnUrl(getBaseUrl(req) + "/checkout/confirm")
                    .cancelUrl(getBaseUrl(req) + "/checkout/cancel.jsp")
                    .item(item)
                    .build();

            CheckoutResponseData checkoutData = payOS.createPaymentLink(paymentData);

            resp.sendRedirect(checkoutData.getCheckoutUrl());

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/checkout/error.jsp");
        }
    }

    private void handleWebhook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JsonNode body = mapper.readTree(req.getInputStream());
            Webhook webhook = mapper.treeToValue(body, Webhook.class);

            WebhookData data = payOS.verifyPaymentWebhookData(webhook);
            System.out.println("✅ Webhook Verified: " + data);

            long orderCode = data.getOrderCode();

            List<CartItem> cartItems = getCartItemsFromPendingCartCookie(req);
            String phone = getCookieValue(req, "pendingPhone");
            String address = getCookieValue(req, "pendingAddress");

            if (cartItems.isEmpty() || phone == null || address == null) {
                System.out.println("❌ Thiếu dữ liệu giỏ hàng hoặc địa chỉ!");
                resp.sendError(400, "Missing cart or shipping info");
                return;
            }

            double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

            Order order = Order.builder()
                    .orderId((int) orderCode)
                    .phone(URLDecoder.decode(phone, "UTF-8"))
                    .shipAddress(URLDecoder.decode(address, "UTF-8"))
                    .totalPrice(total)
                    .paymentStatus("Paid")
                    .status(true)
                    .orderDate(new Date())
                    .userId(getUserIdFromSession(req))
                    .build();

            try (Connection conn = new DBContext().getConnection()) {
                new OrderService(conn).save(order);
                OrderItemService orderItemService = new OrderItemService(conn);
                for (CartItem item : cartItems) {
                    orderItemService.save(OrderItem.builder()
                            .orderId((int) orderCode)
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build());
                }
            }

            System.out.println("✅ Order & Items đã lưu (webhook)");

            ObjectNode responseJson = mapper.createObjectNode();
            responseJson.put("error", 0);
            responseJson.put("message", "Webhook delivered");
            resp.setContentType("application/json");
            resp.getWriter().write(responseJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            ObjectNode responseJson = mapper.createObjectNode();
            responseJson.put("error", -1);
            responseJson.put("message", e.getMessage());
            resp.setContentType("application/json");
            resp.getWriter().write(responseJson.toString());
        }
    }

    private long generateOrderCode() {
        String timePart = String.valueOf(System.currentTimeMillis());
        String last6 = timePart.substring(timePart.length() - 6);
        int random = (int) (Math.random() * 9000) + 1000;
        return Long.parseLong(last6 + random);
    }

    private List<CartItem> getCartItemsFromCookie(HttpServletRequest req) {
        List<CartItem> cartItems = new ArrayList<>();
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cart".equals(cookie.getName())) {
                    String value = java.net.URLDecoder.decode(cookie.getValue(), java.nio.charset.StandardCharsets.UTF_8);
                    String[] pairs = value.split(",");
                    for (String pair : pairs) {
                        String[] parts = pair.split(":");
                        if (parts.length == 2) {
                            try {
                                int productId = Integer.parseInt(parts[0]);
                                int quantity = Integer.parseInt(parts[1]);
                                double price = getPriceByProductId(productId);
                                cartItems.add(CartItem.builder()
                                        .productId(productId)
                                        .quantity(quantity)
                                        .price(price)
                                        .build());
                            } catch (Exception ignore) {
                            }
                        }
                    }
                }
            }
        }

        return cartItems;
    }

    private double getPriceByProductId(int productId) {
        ProductDAO dao = new ProductDAO();
        Product product = dao.getProductById(productId);
        return product != null ? product.getPrice() : 0;
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        String url = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url += ":" + serverPort;
        }
        url += contextPath;
        return url;
    }

    private int getUserIdFromSessionOrRequest(HttpServletRequest req) {
        HttpSession session = req.getSession(false); // không tạo mới nếu chưa có session
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return user.getUserId();
        }
        // Chưa login ➜ trả về 0
        return 0;
    }

    private List<CartItem> getCartItemsFromPendingCartCookie(HttpServletRequest req) {
        List<CartItem> cartItems = new ArrayList<>();
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("pendingCart".equals(cookie.getName())) {
                    try {
                        String decoded = new String(Base64.getDecoder().decode(cookie.getValue()));
                        CartItem[] items = mapper.readValue(decoded, CartItem[].class);
                        cartItems = List.of(items);
                    } catch (Exception ignore) {}
                }
            }
        }
        return cartItems;
    }

    private String getCookieValue(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private int getUserIdFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Integer userId = user.getUserId();
            return userId != null ? userId : 0; // fallback an toàn
        }
        return 0;
    }


}

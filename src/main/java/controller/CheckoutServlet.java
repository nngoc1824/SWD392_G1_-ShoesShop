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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout/create", "/checkout/confirm-webhook"})
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

            double total = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

            String address = req.getParameter("address");
            String province = req.getParameter("province");
            String district = req.getParameter("district");
            String ward = req.getParameter("ward");
            String fullAddress = address + ", " + ward + ", " + district + ", " + province;

            long orderCode = generateOrderCode();

            String description = "Don hang " + orderCode;

            ItemData item = ItemData.builder()
                    .name(description)
                    .quantity(1)
                    .price((int) total)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount((int) total)
                    .description(description)
                    .returnUrl(getBaseUrl(req) + "/checkout/success.jsp")
                    .cancelUrl(getBaseUrl(req) + "/checkout/cancel.jsp")
                    .item(item)
                    .build();

            Order pendingOrder = Order.builder()
                    .orderId((int) orderCode) // Gán ID từ orderCode đã sinh!
                    .shipAddress(fullAddress)
                    .totalPrice(total)
                    .status(false)
                    .paymentStatus("Pending")
                    .orderDate(new Date())
                    .phone(phone) // Đừng quên nhận phone từ form!
                    .userId(getUserIdFromSessionOrRequest(req)) // nếu có userId
                    .build();


            req.getSession().setAttribute("pendingOrder", pendingOrder);

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

            HttpSession session = req.getSession();
            Order pendingOrder = (Order) session.getAttribute("pendingOrder");

            if (pendingOrder != null && pendingOrder.getOrderId() == orderCode) {
                pendingOrder.setStatus(true);
                pendingOrder.setPaymentStatus("Paid");
                pendingOrder.setOrderDate(new Date());

                try (Connection conn = new DBContext().getConnection()) {
                    OrderService service = new OrderService(conn);
                    service.save(pendingOrder);
                }
                try (Connection conn = new DBContext().getConnection()) {
                    OrderItemService orderItemService = new  OrderItemService(conn);
                    List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
                    for (CartItem cartItem : cartItems) {
                        OrderItem orderItem = OrderItem.builder()
                                .orderId((int) orderCode)
                                .productId(cartItem.getProductId())
                                .quantity(cartItem.getQuantity())
                                .price(cartItem.getPrice())
                                .build();
                        orderItemService.save(orderItem);
                    }
                }

                session.removeAttribute("pendingOrder");
                System.out.println("✅ Đơn hàng đã lưu vào DB.");
            } else {
                System.out.println("⚠️ Không tìm thấy đơn hàng khớp trong session.");
            }

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
}

package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ProductDAO;
import entites.CartItem;
import entites.Order;
import entites.Product;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.OrderService;
import utils.DBContext;
import utils.PayOSInitializer;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/checkout/*")
public class CheckoutServlet extends HttpServlet {
    private final PayOS payOS = PayOSInitializer.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        ObjectNode responseJson = mapper.createObjectNode();

        if ("/create".equals(path)) {
            try {
                // Đọc giỏ hàng từ cookie
                List<CartItem> cartItems = getCartItemsFromCookie(req);

                if (cartItems.isEmpty()) {
                    resp.sendRedirect("/cart.jsp");
                    return;
                }

                // Lấy dữ liệu form
                String firstName = req.getParameter("firstName");
                String lastName = req.getParameter("lastName");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");
                String address = req.getParameter("address");
                String province = req.getParameter("province");
                String district = req.getParameter("district");
                String ward = req.getParameter("ward");

                String fullAddress = address + ", " + ward + ", " + district + ", " + province;

                // Tính tổng tiền từ giỏ hàng
                double total = 0;
                for (CartItem item : cartItems) {
                    total += item.getPrice() * item.getQuantity();
                }

                long orderCode = System.currentTimeMillis() % 1_000_000;
                String description = "Thanh toán đơn hàng của " + firstName + " " + lastName;

                ItemData item = ItemData.builder()
                        .name(description)
                        .quantity(1)
                        .price((int) total)
                        .build();

                PaymentData paymentData = PaymentData.builder()
                        .orderCode(orderCode)
                        .amount((int) total)
                        .description(description)
                        .returnUrl("http://localhost:8080/checkout/success.jsp")
                        .cancelUrl("http://localhost:8080/checkout/cancel.jsp")
                        .item(item)
                        .build();

                // Tạm dựng đơn hàng và lưu vào session để xử lý sau khi thanh toán
                Order order = Order.builder()
                        .shipAddress(fullAddress)
                        .totalPrice(total)
                        .status(false)
                        .paymentStatus("Pending")
                        .build();

                req.getSession().setAttribute("pendingOrder", order);
                resp.sendRedirect(payOS.createPaymentLink(paymentData).getCheckoutUrl());
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendRedirect("/checkout/error.jsp");
            }
        }
        else if ("/confirm-webhook".equals(path)) {
            try {
                JsonNode payload = mapper.readTree(req.getInputStream());

                // Kiểm tra nếu success = true
                boolean success = payload.has("success") && payload.get("success").asBoolean();
                JsonNode dataNode = payload.get("data");

                long orderCode = (dataNode != null && dataNode.has("orderCode")) ? dataNode.get("orderCode").asLong() : -1;
                String code = (dataNode != null && dataNode.has("code")) ? dataNode.get("code").asText() : "";

                // Nếu thanh toán thành công thì lưu đơn hàng
                if (success && "00".equals(code) && orderCode != -1) {
                    HttpSession session = req.getSession();
                    Order pendingOrder = (Order) session.getAttribute("pendingOrder");

                    if (pendingOrder != null) {
                        pendingOrder.setOrderDate(new java.util.Date());
                        pendingOrder.setStatus(true);
                        pendingOrder.setPaymentStatus("Paid");

                        try (Connection conn = new DBContext().getConnection()) {
                            OrderService service = new OrderService(conn);
                            service.save(pendingOrder);
                        }

                        session.removeAttribute("pendingOrder");
                    } else {
                        System.out.println("⚠️ Không tìm thấy đơn hàng trong session.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private List<CartItem> getCartItemsFromCookie(HttpServletRequest req) {
        List<CartItem> cartItems = new java.util.ArrayList<>();
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

                                // Gọi đến ProductService hoặc fix tạm giá tại đây
                                double price = getPriceByProductId(productId);

                                cartItems.add(CartItem.builder()
                                        .productId(productId)
                                        .quantity(quantity)
                                        .price(price)
                                        .build());
                            } catch (Exception ignore) {}
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

}

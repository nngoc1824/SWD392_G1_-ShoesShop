package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entites.Order;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.OrderService;
import utils.DBContext;
import utils.JsonUtil;
import utils.PayOSInitializer;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.UUID;

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
                // Nhận dữ liệu từ form HTML (form-urlencoded)
                String firstName = req.getParameter("firstName");
                String lastName = req.getParameter("lastName");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");
                String address = req.getParameter("address");
                String province = req.getParameter("province");
                String district = req.getParameter("district");
                String ward = req.getParameter("ward");

                String fullAddress = address + ", " + ward + ", " + district + ", " + province;

                // Tạo Order thủ công từ thông tin form
                Order order = Order.builder()
                        .shipAddress(fullAddress)
                        .totalPrice(50000) // hoặc lấy từ giỏ hàng/session phía server
                        .build();

                long orderCode = System.currentTimeMillis() % 1_000_000;
                String description = "Thanh toán đơn hàng của " + firstName + " " + lastName;

                ItemData item = ItemData.builder()
                        .name(description)
                        .quantity(1)
                        .price((int) order.getTotalPrice())
                        .build();

                PaymentData paymentData = PaymentData.builder()
                        .orderCode(orderCode)
                        .amount((int) order.getTotalPrice())
                        .description(description)
                        .returnUrl("http://localhost:8080/checkout/success.jsp")
                        .cancelUrl("http://localhost:8080/checkout/cancel.jsp")
                        .item(item)
                        .build();

                CheckoutResponseData data = payOS.createPaymentLink(paymentData);
                resp.sendRedirect(data.getCheckoutUrl()); // chuyển hướng luôn đến link thanh toán
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendRedirect("/checkout/error.jsp"); // hoặc tạo trang lỗi tùy bạn
            }
        }

        else if ("/confirm-webhook".equals(path)) {
            try {
                // Lấy thông tin webhook dưới dạng form-urlencoded
                String webhookUrl = req.getParameter("webhookUrl");

                // Nếu frontend gửi bằng JSON thì thay bằng:
                // Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
                // String webhookUrl = body.get("webhookUrl");

                String result = payOS.confirmWebhook(webhookUrl);

                // Trích thông tin từ kết quả xác nhận (giả sử là JSON string)
                JsonNode json = mapper.readTree(result);
                long orderCode = json.has("orderCode") ? json.get("orderCode").asLong() : -1;
                String status = json.has("status") ? json.get("status").asText() : "";

                // Nếu trạng thái là PAID thì cập nhật trong DB
                if ("PAID".equalsIgnoreCase(status) && orderCode != -1) {
                    try (Connection conn = new DBContext().getConnection()) {
                        OrderService service = new OrderService(conn);
                        service.updateStatus(orderCode, true, "Paid");
                    } catch (Exception dbError) {
                        System.out.println("❌ Lỗi khi cập nhật đơn hàng trong DB:");
                        dbError.printStackTrace();
                    }
                }

                responseJson.put("error", 0);
                responseJson.set("data", json);
                responseJson.put("message", "ok");
            } catch (Exception e) {
                e.printStackTrace();
                responseJson.put("error", -1);
                responseJson.put("message", "Lỗi xác minh webhook: " + e.getMessage());
            }

            JsonUtil.sendJson(resp, responseJson);
        }
    }
}
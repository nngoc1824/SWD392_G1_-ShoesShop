package controller.CheckoutController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entites.Order;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.JsonUtil;
import utils.PayOSInitializer;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.io.IOException;
import java.util.Map;
@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {
    private final PayOS payOS = PayOSInitializer.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if ("/create".equals(path)) {
            Order order = mapper.readValue(req.getInputStream(), Order.class);

            long orderCode = System.currentTimeMillis() % 1_000_000;
            String description = "Thanh toán đơn hàng tại " + order.getShipAddress();

            ItemData item = ItemData.builder()
                    .name(description)
                    .quantity(1)
                    .price(order.getTotalPrice().intValue())
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(order.getTotalPrice().intValue())
                    .description(description)
                    .returnUrl("https://yourdomain.com/order/success")
                    .cancelUrl("https://yourdomain.com/order/cancel")
                    .item(item)
                    .build();

            ObjectNode responseJson = mapper.createObjectNode();
            try {
                CheckoutResponseData data = payOS.createPaymentLink(paymentData);
                responseJson.put("error", 0);
                responseJson.put("message", "success");
                responseJson.set("data", mapper.valueToTree(data));
            } catch (Exception e) {
                e.printStackTrace();
                responseJson.put("error", -1);
                responseJson.put("message", e.getMessage());
            }

            JsonUtil.sendJson(resp, responseJson);
        }

        else if ("/confirm-webhook".equals(path)) {
            Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
            String webhook = body.get("webhookUrl");

            ObjectNode responseJson = mapper.createObjectNode();
            try {
                String result = payOS.confirmWebhook(webhook);
                responseJson.put("error", 0);
                responseJson.set("data", mapper.valueToTree(result));
                responseJson.put("message", "ok");
            } catch (Exception e) {
                responseJson.put("error", -1);
                responseJson.put("message", e.getMessage());
            }
            JsonUtil.sendJson(resp, responseJson);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path != null && path.matches("^/\\d+$")) {
            long orderId = Long.parseLong(path.substring(1));
            ObjectNode responseJson = mapper.createObjectNode();

            try {
                PaymentLinkData data = payOS.getPaymentLinkInformation(orderId);
                responseJson.put("error", 0);
                responseJson.put("message", "ok");
                responseJson.set("data", mapper.valueToTree(data));
            } catch (Exception e) {
                responseJson.put("error", -1);
                responseJson.put("message", e.getMessage());
            }
            JsonUtil.sendJson(resp, responseJson);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path != null && path.matches("^/\\d+$")) {
            int orderId = Integer.parseInt(path.substring(1));
            ObjectNode responseJson = mapper.createObjectNode();

            try {
                PaymentLinkData result = payOS.cancelPaymentLink(orderId, null);
                responseJson.put("error", 0);
                responseJson.put("message", "ok");
                responseJson.set("data", mapper.valueToTree(result));
            } catch (Exception e) {
                responseJson.put("error", -1);
                responseJson.put("message", e.getMessage());
            }

            JsonUtil.sendJson(resp, responseJson);
        }
    }
}

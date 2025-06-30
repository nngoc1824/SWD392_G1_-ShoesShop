package controller.CheckoutController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entites.PaymentStatus;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.OrderService;
import utils.JsonUtil;
import utils.PayOSInitializer;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.io.IOException;

@WebServlet("/payment/payos_transfer_handler")
public class PaymentWebhookServlet extends HttpServlet {
    private final PayOS payOS = PayOSInitializer.getInstance();
    private final OrderService orderService = new OrderService(); // giả lập

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectNode responseJson = mapper.createObjectNode();

        try {
            Webhook webhook = mapper.readValue(req.getInputStream(), Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhook);

            Long orderCode = data.getOrderCode();
            String status = data.getCode();

            PaymentStatus newStatus = PaymentStatus.valueOf(status);
            boolean updated = orderService.updateOrderStatus(orderCode, newStatus);

            responseJson.put("error", 0);
            responseJson.put("message", updated ? "Order updated" : "Order not found");
            responseJson.set("data", mapper.valueToTree(data));

        } catch (Exception e) {
            responseJson.put("error", -1);
            responseJson.put("message", e.getMessage());
            responseJson.set("data", null);
            e.printStackTrace();
        }

        JsonUtil.sendJson(resp, responseJson);
    }
}

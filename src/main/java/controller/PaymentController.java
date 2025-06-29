package controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entites.PaymentStatus;
import org.springframework.web.bind.annotation.*;
import service.OrderService;
import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PayOS payOS;
    private final OrderService orderService;

    public PaymentController(PayOS payOS, OrderService orderService) {
        this.payOS = payOS;
        this.orderService = orderService;
    }

    @PostMapping(path = "/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);

            Long orderCode = data.getOrderCode();
            String status = data.getDesc();

            PaymentStatus newStatus = PaymentStatus.valueOf(status);
            boolean updated = orderService.updateOrderStatus(orderCode, newStatus);

            response.put("error", 0);
            response.put("message", updated ? "Order updated" : "Order not found");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }
}
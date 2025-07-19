package dto;

import entites.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object for Order entity to handle JSP mapping requirements.
 * Maps Order entity fields to JSP-expected property names.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private int orderId;
    private String orderCode; // Maps to orderId as string
    private double totalPrice;
    private Date orderDate;
    private boolean isDelivered; // Maps to status field
    private String shipAddress;
    private String paymentStatus;
    private String phone;
    private int userId;

    /**
     * Convert Order entity to OrderDTO
     */
    public static OrderDTO fromOrder(Order order) {
        if (order == null) return null;
        
        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .orderCode(String.valueOf(order.getOrderId())) // Use orderId as orderCode
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .isDelivered(order.isStatus()) // Map status to isDelivered
                .shipAddress(order.getShipAddress())
                .paymentStatus(order.getPaymentStatus())
                .phone(order.getPhone())
                .userId(order.getUserId())
                .build();
    }

    /**
     * Convert OrderDTO back to Order entity
     */
    public Order toOrder() {
        return Order.builder()
                .orderId(this.orderId)
                .totalPrice(this.totalPrice)
                .orderDate(this.orderDate)
                .status(this.isDelivered) // Map isDelivered back to status
                .shipAddress(this.shipAddress)
                .paymentStatus(this.paymentStatus)
                .phone(this.phone)
                .userId(this.userId)
                .build();
    }
}

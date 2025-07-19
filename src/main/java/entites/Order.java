package entites;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private int orderId;
    private double totalPrice;
    private Date orderDate;
    private boolean status;
    private String shipAddress;
    private String paymentStatus;
    private String phone;
    private int userId;

}
package entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private Integer orderId;
    private Double totalPrice;
    private Date orderDate;
    private Boolean status;
    private String shipAddress;
    private String paymentStatus;
}

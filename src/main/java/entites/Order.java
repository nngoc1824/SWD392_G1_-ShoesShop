package entites;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private Long orderCode; // Mã đơn hàng từ PayOS

    private Double totalPrice;

    private LocalDateTime orderDate;

    private Boolean isDelivered;

    private String shipAddress;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}

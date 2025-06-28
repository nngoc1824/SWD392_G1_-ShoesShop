package entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
}


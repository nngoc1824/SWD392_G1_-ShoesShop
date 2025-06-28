package entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private int cartItemId;
    private int cartId;
    private int productId;
    private int quantity;
    private double price;
}

package entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int productId;
    private String productName;
    private String description;
    private double price;
    private double purchaseCost;
    private int stock;
    private boolean status;
    private LocalDate createdOn;
    private LocalDate modifiedOn;
    private Category category;
}

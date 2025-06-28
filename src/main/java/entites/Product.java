package entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private int productId;
    private String productName;
    private String description;
    private double price;
    private double purchaseCost;
    private int stock;
    private int status;
    private String image;
    private Date createdOn;
    private Date modifiedOn;
    private int categoryId;
}

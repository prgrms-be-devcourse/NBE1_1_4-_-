package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.enumType.Category;

@Data
public class OrderItemResponseDTO {

    private String productName;

    private int price;

    private int quantity;

    private Category category;

    public OrderItemResponseDTO(String productName, int price, int quantity, Category category) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public OrderItemResponseDTO() {
    }
}

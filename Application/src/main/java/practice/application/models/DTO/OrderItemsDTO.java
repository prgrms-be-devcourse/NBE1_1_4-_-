package practice.application.models.DTO;

import lombok.Data;

@Data
public class OrderItemsDTO {
    private int quantity;
    private String productId;

    public OrderItemsDTO(int quantity, String productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public OrderItemsDTO() {
    }
}

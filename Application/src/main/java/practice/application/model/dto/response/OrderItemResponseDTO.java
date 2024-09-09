package practice.application.model.dto.response;

import practice.application.model.entity.OrderItemEntity;
import practice.application.model.entity.common.Category;

import java.time.LocalDateTime;

public class OrderItemResponseDTO {
    private Long orderItemId;
    private String productName;
    private int price;
    private int quantity;
    private Category category;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public OrderItemResponseDTO(OrderItemEntity orderItemEntity) {
        this.orderItemId = orderItemEntity.getId();
        this.productName = orderItemEntity.getProduct().getProduct_name();
        this.price = orderItemEntity.getPrice();
        this.quantity = orderItemEntity.getQuantity();
        this.category = orderItemEntity.getCategory();
        this.created_at = orderItemEntity.getCreated_at();
        this.updated_at = orderItemEntity.getUpdated_at();
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
}

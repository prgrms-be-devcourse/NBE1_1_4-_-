package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import practice.application.models.entity.OrderItemEntity;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderItemDTO {
    private long seq;
    private UUID orderId;
    private UUID productId;
    private String category;
    private long price;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderItemDTO(OrderItemEntity orderItemEntity) {
        this.seq = orderItemEntity.getSeq();
        this.orderId = orderItemEntity.getOrderId();
        this.productId = orderItemEntity.getProductId();
        this.category = orderItemEntity.getCategory();
        this.price = orderItemEntity.getPrice();
        this.quantity = orderItemEntity.getQuantity();
        this.createdAt = orderItemEntity.getCreatedAt();
        this.updatedAt = orderItemEntity.getUpdatedAt();
    }
    public OrderItemEntity toOrderItemEntity() {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setSeq(this.seq);
        orderItemEntity.setOrderId(this.orderId);
        orderItemEntity.setProductId(this.productId);
        orderItemEntity.setCategory(this.category);
        orderItemEntity.setPrice(this.price);
        orderItemEntity.setQuantity(this.quantity);
        orderItemEntity.setCreatedAt(this.createdAt);
        orderItemEntity.setUpdatedAt(this.updatedAt);
        return orderItemEntity;
    }
}


package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import practice.application.models.entities.OrderItemEntity;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OrderItemDTO implements DTOContracts<OrderItemEntity> {

    private long id;
    private OrderDTO orderDTO;
    private ProductDTO productDTO;

    /**
     * 배송 아이템 카테고리
     *
     * @see OrderItemCategory
     */
    private OrderItemCategory category;
    private long totalPrice;
    private int quantity;
    private Instant createdAt;

    /**
     * {@inheritDoc}
     *
     * @return {@link OrderItemDTO}
     */
    @Override
    public OrderItemEntity toEntity() {
        OrderItemEntity entity = new OrderItemEntity();

        if (orderDTO != null)
            entity.setOrderEntity(orderDTO.toEntity());

        if (productDTO != null)
            entity.setProductEntity(productDTO.toEntity());

        if (category != null)
            entity.setCategory(category.toString());

        entity.setId(id)
              .setPrice(totalPrice)
              .setQuantity(quantity)
              .setCreatedAt(createdAt);

        return entity;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" + "id=" + id + ", orderDTO=" + (orderDTO == null ? "null" : orderDTO.getOrderId()) +
               ", productDTO=" + (productDTO == null ? "null" : productDTO.getProductId()) + ", category=" + category +
               ", totalPrice=" + totalPrice + ", quantity=" + quantity + ", createdAt=" + createdAt + '}';
    }
}

// TODO 문서화 필요

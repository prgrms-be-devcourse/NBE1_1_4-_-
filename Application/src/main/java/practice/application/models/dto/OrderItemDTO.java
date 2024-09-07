package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import practice.application.models.entities.OrderItemEntity;

import java.time.Instant;

@Getter
@Setter
@ToString
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
    private long price;
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

        entity.setId(id)
              .setOrderEntity(orderDTO.toEntity())
              .setProductEntity(productDTO.toEntity())
              .setCategory(category.toString())
              .setPrice(price)
              .setQuantity(quantity)
              .setCreatedAt(createdAt);

        return entity;
    }
}

// TODO 문서화 필요

package practice.application.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import practice.application.models.dto.OrderItemCategory;
import practice.application.models.dto.OrderItemDTO;

import java.time.Instant;

/**
 * {@code OrderItem} 관련 {@code Entity}
 * <p>
 * {@link OrderEntity} 와 유사.
 *
 * @see OrderEntity
 */
@Entity(name = "order_item_entity")
@Table(name = "order_items")
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class OrderItemEntity implements EntityContracts<OrderItemDTO> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ColumnDefault("(now())")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * @see OrderItemCategory
     */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;

    /**
     * {@inheritDoc}
     *
     * @return {@link OrderItemDTO}
     */
    @Override
    public OrderItemDTO toDTO() {
        OrderItemDTO dto = new OrderItemDTO();

        dto.setId(id)
           .setQuantity(quantity)
           .setCreatedAt(createdAt)
           .setPrice(price)
           .setCategory(OrderItemCategory.valueOf(category))
           .setOrderDTO(orderEntity.toDTO())
           .setProductDTO(productEntity.toDTO());

        return dto;
    }
}
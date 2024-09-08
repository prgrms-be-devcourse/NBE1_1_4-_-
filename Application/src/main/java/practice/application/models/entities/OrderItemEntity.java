package practice.application.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @ColumnDefault("(now())")
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * @see OrderItemCategory
     */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
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

        if (orderEntity != null)
            dto.setOrderDTO(orderEntity.toDTO());

        dto.setId(id)
           .setQuantity(quantity)
           .setCreatedAt(createdAt)
           .setTotalPrice(price)
           .setCategory(OrderItemCategory.valueOf(category))
           .setProductDTO(productEntity.toDTO());

        return dto;
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" + "id=" + id + ", quantity=" + quantity + ", createdAt=" + createdAt + ", price=" +
               price + ", updatedAt=" + updatedAt + ", category='" + category + '\'' + ", orderEntity=" +
               (orderEntity == null ? "null" : orderEntity.getOrderId()) + ", productEntity=" +
               (productEntity == null ? "null" : productEntity.getProductId()) + '}';
    }
}
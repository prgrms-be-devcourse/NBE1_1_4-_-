package practice.application.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import practice.application.models.dto.OrderDTO;
import practice.application.models.dto.OrderStatus;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * {@code Order} 관련 {@code Entity}
 * <p>
 * {@link OrderDTO} 와 유사.
 *
 * @see OrderDTO
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OrderEntity implements EntityContracts<OrderDTO> {
    @Id
    @Column(name = "order_id", nullable = false, length = 16)
    @GeneratedValue
    private UUID orderId;

    @ColumnDefault("(now())")
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @ColumnDefault("(now())")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    /**
     * @see OrderStatus
     */
    @Column(name = "order_status", nullable = false, length = 50)
    private String orderStatus;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "postcode", nullable = false, length = 200)
    private String postcode;

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<OrderItemEntity> orderItems = new LinkedHashSet<>();

    /**
     * {@inheritDoc}
     *
     * @return {@link OrderDTO}
     */
    @Override
    public OrderDTO toDTO() {
        OrderDTO dto = new OrderDTO();

        dto.setOrderId(orderId)
           .setCreatedAt(createdAt)
           .setEmail(email)
           .setOrderStatus(OrderStatus.valueOf(orderStatus))
           .setAddress(address)
           .setPostcode(postcode);

        return dto;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrderEntity{" + "orderId=")
          .append(orderId)
          .append(", createdAt=")
          .append(createdAt)
          .append(", updatedAt=")
          .append(updatedAt)
          .append(", email='")
          .append(email)
          .append('\'')
          .append(", orderStatus='")
          .append(orderStatus)
          .append('\'')
          .append(", address='")
          .append(address)
          .append('\'')
          .append(", postcode='")
          .append(postcode)
          .append('\'')
          .append(", orderItems=[");

        for (OrderItemEntity item : orderItems)
            sb.append(item.getId())
              .append(", ");

        sb.delete(sb.length() - 2, sb.length());
        sb.append("]}");

        return sb.toString();
    }
}
package practice.application.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import practice.application.models.dto.OrderDTO;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
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

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "order_status", nullable = false, length = 50)
    private String orderStatus;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "postcode", nullable = false, length = 200)
    private String postcode;

    @OneToMany(mappedBy = "orderEntity")
    private Set<OrderItemEntity> orderItems = new LinkedHashSet<>();

    @Override
    public OrderDTO toDTO() {
        OrderDTO dto = new OrderDTO();

        dto.setOrderId(orderId)
           .setCreatedAt(createdAt)
           .setEmail(email)
           .setOrderStatus(orderStatus)
           .setAddress(address)
           .setPostcode(postcode)
           .setOrderItemDTOs(orderItems.stream()
                                       .map(OrderItemEntity::toDTO)
                                       .toList());

        return dto;
    }
}
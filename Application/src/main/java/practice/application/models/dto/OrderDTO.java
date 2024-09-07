package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import practice.application.models.entities.OrderEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 클라이언트와 주고받을 {@code Order} 관련 {@code DTO}
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class OrderDTO implements DTOContracts<OrderEntity> {

    private UUID orderId;
    private String email;
    private String address;
    private String postcode;

    /**
     * 배송 상태
     *
     * @see OrderStatus
     */
    private OrderStatus orderStatus;
    private Instant createdAt;

    private List<OrderItemDTO> orderItemDTOs;

    /**
     * {@inheritDoc}
     *
     * @return {@link OrderEntity}
     */
    @Override
    public OrderEntity toEntity() {
        OrderEntity entity = new OrderEntity();

        entity.setOrderId(orderId)
              .setEmail(email)
              .setAddress(address)
              .setPostcode(postcode)
              .setOrderStatus(orderStatus.toString())
              .setCreatedAt(createdAt);

        if (orderItemDTOs != null)
            entity.setOrderItems(orderItemDTOs.stream()
                                              .map(OrderItemDTO::toEntity)
                                              .collect(Collectors.toSet()));

        return entity;
    }
}

// TODO 추후 문서화 필요

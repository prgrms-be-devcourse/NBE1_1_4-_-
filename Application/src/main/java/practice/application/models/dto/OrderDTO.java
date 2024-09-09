package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

        if (orderItemDTOs != null)
            entity.setOrderItems(orderItemDTOs.stream()
                                              .map(OrderItemDTO::toEntity)
                                              .collect(Collectors.toSet()));

        if (orderStatus != null)
            entity.setOrderStatus(orderStatus.toString());

        entity.setOrderId(orderId)
              .setEmail(email)
              .setAddress(address)
              .setPostcode(postcode)
              .setCreatedAt(createdAt);

        return entity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("OrderDTO{" + "orderId=")
          .append(orderId)
          .append(", email='")
          .append(email)
          .append('\'')
          .append(", address='")
          .append(address)
          .append('\'')
          .append(", postcode='")
          .append(postcode)
          .append('\'')
          .append(", orderStatus=")
          .append(orderStatus)
          .append(", createdAt=")
          .append(createdAt)
          .append(", orderItemDTOs=[");

        for (OrderItemDTO orderItemDTO : orderItemDTOs)
            sb.append(orderItemDTO.getId())
              .append(", ");

        sb.delete(sb.length() - 2, sb.length());
        sb.append("]}");

        return sb.toString();
    }
}

// TODO 추후 문서화 필요

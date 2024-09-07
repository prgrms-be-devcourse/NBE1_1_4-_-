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
    private String orderStatus;
    private Instant createdAt;

    private List<OrderItemDTO> orderItemDTOs;

    @Override
    public OrderEntity toEntity() {
        OrderEntity entity = new OrderEntity();

        entity.setOrderId(orderId)
              .setEmail(email)
              .setAddress(address)
              .setPostcode(postcode)
              .setOrderStatus(orderStatus)
              .setCreatedAt(createdAt)
              .setOrderItems(orderItemDTOs.stream()
                                          .map(OrderItemDTO::toEntity)
                                          .collect(Collectors.toSet()));

        return entity;
    }
}

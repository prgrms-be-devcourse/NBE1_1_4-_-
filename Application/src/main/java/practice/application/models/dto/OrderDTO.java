package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderDTO {

    private String orderId;
    private String email;
    private String address;
    private String postcode;
    private String orderStatus;
    private Instant createdAt;

    private List<OrderItemDTO> orderItems;

    //    public static OrderEntity toEntity(final OrderDTO dto) {
    //        OrderEntity entity = new OrderEntity();
    //        entity.setOrderId(dto.getOrderId());
    //        entity.setEmail(dto.getEmail());
    //        entity.setAddress(dto.getAddress());
    //        entity.setPostcode(dto.getPostcode());
    //        entity.setOrderStatus(dto.getOrderStatus());
    //        entity.setCreatedAt(dto.getCreatedAt());
    //        entity.setOrderItemEntities(dto.getOrderItems()
    //                                       .stream()
    //                                       .map(OrderItemDTO::toEntity)
    //                                       .collect(Collectors.toSet()));
    //        return entity;
    //    }
}

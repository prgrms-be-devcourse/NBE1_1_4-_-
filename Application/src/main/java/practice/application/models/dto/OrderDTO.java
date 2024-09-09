package practice.application.models.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import practice.application.models.OrderStatus;
import practice.application.models.entity.OrderEntity;
import practice.application.models.entity.OrderItemEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private String email;
    private String address;
    private String postcode;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDTO> orderItems;


    public OrderDTO(OrderEntity orderEntity) {
        this.orderId=orderEntity.getOrderId();
        this.email=orderEntity.getEmail();
        this.address=orderEntity.getAddress();
        this.postcode=orderEntity.getPostcode();
        this.orderStatus=orderEntity.getOrderStatus();
        this.createdAt=orderEntity.getCreatedAt();
        this.updatedAt=orderEntity.getUpdatedAt();
//        list 내 entity 객체를 dto객체들로 변환
        this.orderItems=toOrderItemDTOs(orderEntity.getOrderItems());
    }

    public OrderEntity toEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderId);
        orderEntity.setEmail(email);
        orderEntity.setAddress(address);
        orderEntity.setPostcode(postcode);
        orderEntity.setOrderStatus(orderStatus);
        orderEntity.setCreatedAt(createdAt);
        orderEntity.setUpdatedAt(updatedAt);
        return orderEntity;
    }

//    orderItemEntities리스트를 orderItemDTO리스트로 변환
    public List<OrderItemDTO> toOrderItemDTOs(List<OrderItemEntity>orderItemEntities) {
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItemDTOS.add(new OrderItemDTO(orderItemEntity));
        }
        return orderItemDTOS;
    }
}

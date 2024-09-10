package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.OrderEntity;

import java.util.List;

@Data
public class OrderResponseDTO {

    private String email;

    private String postCode;

    private int sum;

    private List<OrderItemResponseDTO> orderItemResponseDTOS;

    public OrderResponseDTO(OrderEntity orderEntity) {
        this.email = orderEntity.getEmail();
        this.sum = orderEntity.getSum();
        this.postCode = orderEntity.getPostCode();
        this.orderItemResponseDTOS = orderEntity.getOrdersItemsList().stream().map(ordersItemEntity -> new OrderItemResponseDTO(ordersItemEntity.getProduct().getProductName(),
                ordersItemEntity.getPrice(), ordersItemEntity.getQuantity(), ordersItemEntity.getCategory())).toList();
    }

    public OrderResponseDTO() {
    }
}

package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.OrderEntity;

@Data
public class OrderCreateResponseDTO {
    private String orderId;

    private int sum;

    private String value;

    public OrderCreateResponseDTO(OrderEntity orderEntity) {
        this.orderId = orderEntity.getId();
        this.sum = orderEntity.getSum();
        this.value = "당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다.";
    }

    public OrderCreateResponseDTO() {
    }
}

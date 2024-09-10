package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.enumType.OrderStatus;

@Data
public class PatchOrderStatus {

    private OrderStatus status;

    public PatchOrderStatus(OrderStatus status) {
        this.status = status;
    }

    public PatchOrderStatus() {
    }
}

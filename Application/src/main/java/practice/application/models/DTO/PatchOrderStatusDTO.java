package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.enumType.OrderStatus;

@Data
public class PatchOrderStatusDTO {

    private OrderStatus status;

    public PatchOrderStatusDTO(OrderStatus status) {
        this.status = status;
    }

    public PatchOrderStatusDTO() {
    }
}

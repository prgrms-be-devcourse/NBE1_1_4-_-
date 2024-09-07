package practice.application.models.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {

    private String email;

    private String postCode;

    List<OrderItemsDTO> orderItemsDTOS;

    public OrderCreateDTO(String email, String postCode, List<OrderItemsDTO> orderItemsDTOS) {
        this.email = email;
        this.postCode = postCode;
        this.orderItemsDTOS = orderItemsDTOS;
    }

    public OrderCreateDTO() {
    }
}

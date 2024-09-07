package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderItemDTO {

    private long id;
    private OrderDTO orderDTO;
    private ProductDTO productDTO;
    private String category;
    private long price;
    private int quantity;
    private Instant createdAt;

    //    public static OrderItemEntity toEntity(final OrderItemDTO dto) {
    //        OrderItemEntity entity = new OrderItemEntity();
    //        entity.setId(dto.getId());
    //        entity.setOrderEntity(OrderDTO.toEntity(dto.getOrderDTO()));
    //        entity.setProductEntity(ProductDTO.toEntity(dto.getProductDTO()));
    //        entity.setCategory(dto.getCategory());
    //        entity.setPrice(dto.getPrice());
    //        entity.setQuantity(dto.getQuantity());
    //        entity.setCreatedAt(dto.getCreatedAt());
    //        return entity;
    //    }
}

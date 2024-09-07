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
public class ProductDTO {
    private String productId;
    private String productName;
    private String category;
    private long price;
    private String description;
    private Instant createdAt;


    //    public static ProductEntity toEntity(final ProductDTO dto) {
    //        ProductEntity entity = new ProductEntity();
    //        entity.setProductId(dto.getProductId());
    //        entity.setProductName(dto.getProductName());
    //        entity.setCategory(dto.getCategory());
    //        entity.setPrice(dto.getPrice());
    //        entity.setDescription(dto.getDescription());
    //        entity.setCreatedAt(dto.getCreatedAt());
    //        return entity;
    //    }
}

package practice.application.models.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import practice.application.models.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductDTO {
    private UUID productId;
    private String productName;
    private String category;
    private long price;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime  updatedAt;


    public ProductDTO(ProductEntity productEntity) {
        this.productId = productEntity.getProductId();
        this.productName = productEntity.getProductName();
        this.category = productEntity.getCategory();
        this.price = productEntity.getPrice();
        this.description = productEntity.getDescription();
        this.createdAt = productEntity.getCreatedAt();
        this.updatedAt = productEntity.getUpdatedAt();
    }
    public ProductEntity toProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(this.productId);
        productEntity.setProductName(this.productName);
        productEntity.setCategory(this.category);
        productEntity.setPrice(this.price);
        productEntity.setDescription(this.description);
        productEntity.setCreatedAt(this.createdAt);
        productEntity.setUpdatedAt(this.updatedAt);
        return productEntity;
    }

}

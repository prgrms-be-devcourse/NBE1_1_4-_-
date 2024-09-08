package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import practice.application.models.entities.ProductEntity;

import java.time.Instant;
import java.util.UUID;


/**
 * 클라이언트와 주고받을 {@code Product} 관련 {@code DTO}
 *
 * @see practice.application.controllers.ProductController
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ProductDTO implements DTOContracts<ProductEntity> {

    /**
     * 제품 ID
     */
    private UUID productId;

    /**
     * 제품 이름
     */
    private String productName;

    /**
     * 제품 카테고리
     *
     * @see ProductCategory
     */
    private ProductCategory category;

    /**
     * 제품 가격
     */
    private long price;

    /**
     * 제품 상세 설명
     */
    private String description;

    /**
     * 제품 등록 시각
     */
    private Instant createdAt;


    /**
     * {@inheritDoc}
     *
     * @return {@link ProductEntity}
     */
    @Override
    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();

        if (category != null)
            entity.setCategory(category.toString());

        entity.setProductId(productId)
              .setProductName(productName)
              .setPrice(price)
              .setDescription(description)
              .setCreatedAt(createdAt);

        return entity;
    }

    @Override
    public String toString() {
        return "ProductDTO{" + "productId=" + productId + ", productName='" + productName + '\'' + ", category=" +
               category + ", price=" + price + ", description='" + description + '\'' + ", createdAt=" + createdAt +
               "}";
    }
}

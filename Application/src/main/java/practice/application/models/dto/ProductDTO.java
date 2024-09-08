package practice.application.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import practice.application.models.entities.ProductEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * 클라이언트와 주고받을 {@code Product} 관련 {@code DTO}
 *
 * @see practice.application.controllers.ProductController
 */
@Getter
@Setter
@ToString
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
     * 연관된 {@link OrderItemDTO} 들
     */
    private List<OrderItemDTO> orderItemDTOs;

    /**
     * {@inheritDoc}
     *
     * @return {@link ProductEntity}
     */
    @Override
    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();

        entity.setProductId(productId)
              .setProductName(productName)
              .setCategory(category.toString())
              .setPrice(price)
              .setDescription(description)
              .setCreatedAt(createdAt);

        if (orderItemDTOs != null)
            entity.setOrderItems(orderItemDTOs.stream()
                                              .map(OrderItemDTO::toEntity)
                                              .collect(Collectors.toSet()));

        return entity;
    }
}

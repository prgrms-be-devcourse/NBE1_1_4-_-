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

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class ProductDTO implements DTOContracts<ProductEntity> {

    private UUID productId;
    private String productName;
    private ProductCategory category;
    private long price;
    private String description;
    private Instant createdAt;

    private List<OrderItemDTO> orderItemDTOs;

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

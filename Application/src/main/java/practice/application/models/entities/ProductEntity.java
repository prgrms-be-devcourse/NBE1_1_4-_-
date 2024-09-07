package practice.application.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import practice.application.models.dto.ProductCategory;
import practice.application.models.dto.ProductDTO;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "productEntity")
@Table(name = "products")
@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class ProductEntity implements EntityContracts<ProductDTO> {
    @Id
    @Column(name = "product_id", nullable = false, length = 16)
    @GeneratedValue
    private UUID productId;

    @ColumnDefault("(now())")
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "product_name", nullable = false, length = 20)
    private String productName;

    @ColumnDefault("'UNKNOWN'")
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @ColumnDefault("'No description'")
    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "productEntity")
    private Set<OrderItemEntity> orderItems = new LinkedHashSet<>();

    @Override
    public ProductDTO toDTO() {
        ProductDTO dto = new ProductDTO();

        dto.setProductId(productId)
           .setCreatedAt(createdAt)
           .setPrice(price)
           .setProductName(productName)
           .setCategory(ProductCategory.valueOf(category))
           .setDescription(description);

        if (orderItems != null)
            dto.setOrderItemDTOs(orderItems.stream()
                                           .map(OrderItemEntity::toDTO)
                                           .toList());

        return dto;
    }
}
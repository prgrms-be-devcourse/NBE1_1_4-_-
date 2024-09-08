package practice.application.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import practice.application.models.dto.ProductCategory;
import practice.application.models.dto.ProductDTO;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * {@code Product} 관련 {@code Entity}
 * <p>
 * {@link ProductDTO} 와 유사.
 *
 * @see ProductDTO
 */
@Entity(name = "productEntity")
@Table(name = "products")
@Getter
@Setter
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

    @ColumnDefault("(now())")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "product_name", nullable = false, length = 20)
    private String productName;

    /**
     * @see ProductCategory
     */
    @ColumnDefault("'UNKNOWN'")
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @ColumnDefault("'No description'")
    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY)
    @Column(nullable = true)
    private Set<OrderItemEntity> orderItems = new LinkedHashSet<>();

    /**
     * {@inheritDoc}
     *
     * @return {@link ProductDTO}
     */
    @Override
    public ProductDTO toDTO() {
        ProductDTO dto = new ProductDTO();

        dto.setProductId(productId)
           .setCreatedAt(createdAt)
           .setPrice(price)
           .setProductName(productName)
           .setCategory(ProductCategory.valueOf(category))
           .setDescription(description);

        return dto;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProductEntity{" + "productId=")
          .append(productId)
          .append(", createdAt=")
          .append(createdAt)
          .append(", price=")
          .append(price)
          .append(", updatedAt=")
          .append(updatedAt)
          .append(", productName='")
          .append(productName)
          .append('\'')
          .append(", category='")
          .append(category)
          .append('\'')
          .append(", description='")
          .append(description)
          .append('\'')
          .append(", orderItems=[");

        for (OrderItemEntity orderItem : orderItems)
            sb.append(orderItem.getId())
              .append(", ");

        sb.delete(sb.length() - 2, sb.length());
        sb.append("]}");

        return sb.toString();
    }
}
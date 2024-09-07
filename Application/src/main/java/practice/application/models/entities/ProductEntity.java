package practice.application.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductEntity {
    @Id
    @Column(name = "product_id", nullable = false, length = 16)
    private UUID productId;

    @ColumnDefault("(now())")
    @Column(name = "created_at", nullable = false)
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

}
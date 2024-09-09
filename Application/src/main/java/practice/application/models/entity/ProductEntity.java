package practice.application.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@ToString
@Table(name="products")
public class ProductEntity {
    @Id
    @Column(name="product_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동 생성 전략 사용
    private UUID productId;

    @OneToMany(mappedBy = "product")
    private List<OrderItemEntity>orderItems=new ArrayList<>();

    @Column(nullable = false,length=20,name="product_name")
    private String productName;

    @Column(nullable = false,length=50)
    private String category;

    @Column(nullable = false)
    private long price;

    @Column(length=500)
    private String description;

    @CreationTimestamp @Column(name="created_at",nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

    @UpdateTimestamp @Column(name="updated_at",columnDefinition = "DATETIME(6)")
    private LocalDateTime  updatedAt;
}

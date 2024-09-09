package practice.application.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="order_items")
public class OrderItemEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long seq;

    @Column(name="order_id",nullable = false)
    private UUID orderId;

    @Column(name="product_id",nullable = false)
    private UUID productId;

    @Column(nullable = false,length=50)
    private String category;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private int quantity;

    @CreationTimestamp
    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="order_id", insertable = false, updatable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name="product_id", insertable = false, updatable = false)
    private ProductEntity product;
}



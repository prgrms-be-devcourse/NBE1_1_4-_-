package practice.application.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Order;
import practice.application.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name="orders")
@ToString
public class OrderEntity {
    @Id
    @Column(name = "order_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동 생성 전략 사용
    private UUID orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems=new ArrayList<>();

    @Column(nullable = false,length=50)
    private String email;

    @Column(nullable = false,length=200)
    private String address;

    @Column(nullable = false,length=200)
    private String postcode;

    @Enumerated(EnumType.STRING)
    @Column(name="order_status",nullable = false,length=50)
    private OrderStatus orderStatus;

//    MySQL의 DATETIME(6) 타입에 맞춰 마이크로초까지 지정
    @CreationTimestamp @Column(name="created_at",nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime createdAt;

//엔티티가 수정될때 자동 업데이트 되도록 설정
    @UpdateTimestamp @Column(name="updated_at",columnDefinition = "DATETIME(6)")
    private LocalDateTime updatedAt;


}

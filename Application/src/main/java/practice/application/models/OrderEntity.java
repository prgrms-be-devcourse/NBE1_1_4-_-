package practice.application.models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import practice.application.models.enumType.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")

public class OrderEntity extends BaseEntity{
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;

    private String postCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    private List<OrdersItemEntity> ordersItemsList = new ArrayList<>();

}

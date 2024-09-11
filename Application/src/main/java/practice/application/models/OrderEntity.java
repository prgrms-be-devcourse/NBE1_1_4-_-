package practice.application.models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.application.models.enumType.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")

public class OrderEntity extends BaseEntity{
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;

    private String postCode;

    private int sum = 0;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrdersItemEntity> ordersItemsList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    public OrderEntity(String email, String postCode, List<OrdersItemEntity> ordersItemsList) {
        this.email = email;
        this.postCode = postCode;
//        this.status = OrderStatus.ORDER;
        this.status = OrderStatus.RESERVED;
        for(OrdersItemEntity ordersItem : ordersItemsList){  // 주문 시  총 값
            addOrderItem(ordersItem);
            this.sum += ordersItem.getPrice();
        }
    }

    public void addOrderItem(OrdersItemEntity ordersItem) { //양방향
        ordersItemsList.add(ordersItem);
        ordersItem.addOrderEntity(this);
    }

    public void addMember(MemberEntity member) { // 연관관계 매핑
        this.member = member;
        member.getOrderEntityList().add(this);
    }

    public void orderCancel(){  //주문 취소 로직
        this.status = OrderStatus.CANCELED;

        for(OrdersItemEntity ordersItem : ordersItemsList){
            int quantity = ordersItem.getQuantity();

            ordersItem.getProduct().addQuantity(quantity);
        }
    }



}

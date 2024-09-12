package practice.application.models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.application.models.enumType.Category;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "order_items")
public class OrdersItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id" )
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int price;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;


    public void addOrderItem( ProductEntity product) {
        this.product = product;
        product.getOrdersItems().add(this);
    }

    public int calculatePrice(ProductEntity productEntity){  // 해당 주문 가격
        return productEntity.getPrice() * quantity;
    }

    public void addOrderEntity(OrderEntity orderEntity) {
        this.order = orderEntity;
    }

    public OrdersItemEntity( int quantity, ProductEntity product) {
        this.category = product.getCategory();
        this.quantity = quantity; //주문 수량
        this.price = calculatePrice(product);
        addOrderItem(product);
        product.removeQuantity(quantity);
    }
}

package practice.application.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class ProductEntity extends BaseEntity{

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String productName;

    private String category;

    private int price;

    private String description;

    @OneToMany(mappedBy = "product")
    List<OrdersItemEntity> ordersItems = new ArrayList<>();

    public ProductEntity(String productName, String category, int price, String description) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
    }  // 제품 생성하면서 UUID 넣어줌


}

package practice.application.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.application.models.enumType.Category;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "products")
public class ProductEntity extends BaseEntity{

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String productName;

    @Enumerated(EnumType.STRING)
    private Category category;     //이건 추후에 엔티티로 구현 에정

    private int price;

    private int quantity;

    private String description;


    @OneToMany(mappedBy = "product")
    List<OrdersItemEntity> ordersItems = new ArrayList<>();

    public ProductEntity(String productName, Category category, int price, int quantity, String description) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }


    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        this.quantity -= quantity;
    }
}

package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.ProductEntity;
import practice.application.models.enumType.Category;

@Data
public class ProductsAllResponseDTO {

     private String id;

    private String productName;

    private Category category;

    private int price;

    public ProductsAllResponseDTO(ProductEntity product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.category = product.getCategory();
        this.price = product.getPrice();
    }

    public ProductsAllResponseDTO() {
    }
}

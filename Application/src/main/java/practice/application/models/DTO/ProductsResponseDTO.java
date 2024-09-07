package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.ProductEntity;
import practice.application.models.enumType.Category;

@Data
public class ProductsResponseDTO {

    private String productName;

    private Category category;

    private int price;

    private String description;

    public ProductsResponseDTO(ProductEntity productEntity) {
        this.productName = productEntity.getProductName();
        this.category = productEntity.getCategory();
        this.price = productEntity.getPrice();
        this.description = productEntity.getDescription();
    }

    public ProductsResponseDTO() {
    }
}

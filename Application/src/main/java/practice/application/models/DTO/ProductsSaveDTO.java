package practice.application.models.DTO;

import lombok.Data;
import practice.application.models.ProductEntity;
import practice.application.models.enumType.Category;

@Data
public class ProductsSaveDTO {

    private String id;

    private String productName;

    private Category category;

    private int price;

    private int quantity;

    private String description;

    public ProductsSaveDTO(String productName, Category category, int price, int quantity, String description) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }




    public ProductEntity dtoToEntity() {
      return  new ProductEntity(this.productName, this.category, this.price, this.quantity, this.description);
    }


}

package practice.application.model.dto.response;

import practice.application.model.entity.common.Category;
import practice.application.model.entity.ProductEntity;

import java.time.LocalDateTime;

public class ProductResponseDTO {
    private Long id;
    private String product_name;
    private Category category;
    private int price;
    private String description;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public ProductResponseDTO(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.product_name = productEntity.getProduct_name();
        this.category = productEntity.getCategory();
        this.price = productEntity.getPrice();
        this.description = productEntity.getDescription();
        this.created_at = productEntity.getCreated_at();
        this.updated_at = productEntity.getUpdated_at();
    }

    public Long getId() {
        return id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Category getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
}

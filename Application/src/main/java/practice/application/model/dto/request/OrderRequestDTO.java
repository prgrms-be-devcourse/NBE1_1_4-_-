package practice.application.model.dto.request;

public class OrderRequestDTO {
    private int quantity;
    private Long productId;

    public OrderRequestDTO() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

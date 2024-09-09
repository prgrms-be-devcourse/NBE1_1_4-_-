package practice.application.models.dto;

/**
 * {@code OrderItem} 내 {@code category} 지칭하기 위한 {@code enum} 상수들.
 *
 * <li>ELECTRONICS
 * <li>FOOD
 * <li>SUPPLY
 * <li>MECHANICS
 * <li>FURNITURE
 * <li>CLOTH
 * <li>OTHER
 *
 * @see OrderItemDTO
 * @see practice.application.models.entities.OrderItemEntity
 */
public enum OrderItemCategory {
    ELECTRONICS, FOOD, SUPPLY, MECHANICS, FURNITURE, CLOTH, OTHER
}

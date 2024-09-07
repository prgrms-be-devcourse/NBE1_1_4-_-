package practice.application.models.dto;

/**
 * {@code Order} 내 {@code orderStatus} 지칭하기 위한 {@code enum} 상수들.
 *
 * <li>{@code PACKAGING} : 아직 물품 배송 준비중
 * <li>{@code COMPLETED} : 배송 완료
 *
 * @see OrderDTO
 * @see practice.application.models.entities.OrderEntity
 */
public enum OrderStatus {
    PACKAGING, COMPLETED
}

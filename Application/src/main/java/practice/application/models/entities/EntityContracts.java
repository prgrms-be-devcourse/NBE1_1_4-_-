package practice.application.models.entities;

/**
 * 여러 {@code Entity} 들에 {@link #toDTO()} 같은 메서드 안까먹으려 만든 인터페이스
 *
 * @param <D> {@code Entity} 와 연관된 {@code DTO} 타입
 * @see practice.application.models.dto.DTOContracts
 */
public interface EntityContracts<D> {
    /**
     * {@code Entity} 에서 {@code DTO} 로 변환
     *
     * @return {@code <D>}
     */
    D toDTO();
}

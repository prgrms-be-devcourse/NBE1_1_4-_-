package practice.application.models.dto;

/**
 * 여러 {@code DTO} 들에 {@link #toEntity()} 같은 메서드 안까먹으려 만든 인터페이스
 *
 * @param <E> {@code DTO} 와 연관된 {@code Entity} 타입
 * @see practice.application.models.entities.EntityContracts
 */
public interface DTOContracts<E> {
    /**
     * {@code DTO} 에서 {@code Entity} 로 변환
     *
     * @return {@code <E>}
     */
    E toEntity();
}

package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.models.entities.ProductEntity;

import java.util.List;
import java.util.UUID;

/**
 * <h5>{@code Product} DB DAO interface</h5>
 *
 * @see practice.application.services.ProductService
 */
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    /**
     * 제품 중 특정 {@code category} 에 속한 모든 제품을 가져오는 메서드
     *
     * @param category 검색할 카테고리
     * @return {@code List<}{@link ProductEntity}{@code >}
     * @see practice.application.services.ProductService
     */
    List<ProductEntity> findAllByCategory(String category);
}
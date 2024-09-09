package practice.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.model.entity.common.Category;
import practice.application.model.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByCategory(Category category);
    Optional<ProductEntity> findById(Long productId);
}

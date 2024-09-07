package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.models.entities.ProductEntity;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    List<ProductEntity> findAllByCategory(String category);
}

package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.models.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {}

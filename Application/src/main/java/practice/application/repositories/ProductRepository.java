package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.application.models.ProductEntity;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Query("select p from ProductEntity p where p.id IN :productId")
    List<ProductEntity> findByProductId(@Param("productId") List<String> productId);
}

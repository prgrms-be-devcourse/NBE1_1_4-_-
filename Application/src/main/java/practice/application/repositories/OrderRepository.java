package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.application.models.entity.OrderEntity;

import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByEmail(String email);
}

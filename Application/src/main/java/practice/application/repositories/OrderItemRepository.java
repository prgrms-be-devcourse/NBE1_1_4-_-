package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.models.entities.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {}

// TODO 추후 문서화 필요
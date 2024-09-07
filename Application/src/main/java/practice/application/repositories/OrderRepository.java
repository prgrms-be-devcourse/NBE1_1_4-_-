package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.models.entities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {}

package practice.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.model.entity.OrderEntity;
import practice.application.model.entity.common.OrderStatus;
import practice.application.model.entity.UserEntity;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByUserAndOrderStatus(UserEntity user, OrderStatus orderStatus);
}

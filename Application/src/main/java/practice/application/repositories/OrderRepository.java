package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practice.application.models.entities.OrderEntity;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.orderItems JOIN FETCH productEntity")
    List<OrderEntity> findAll();

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderItems JOIN FETCH productEntity WHERE o.orderStatus = :orderStatus")
    List<OrderEntity> findAllByOrderStatus(String orderStatus);

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderItems JOIN FETCH productEntity WHERE o.orderId = :orderId")
    OrderEntity findByOrderId(UUID orderId);
}

// TODO 추후 문서화 필요
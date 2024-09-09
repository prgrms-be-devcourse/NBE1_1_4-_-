package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import practice.application.models.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByEmail(String email);
    @Query("SELECT o FROM OrderEntity o WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay")
    List<OrderEntity> findOrdersByDateRange(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

}

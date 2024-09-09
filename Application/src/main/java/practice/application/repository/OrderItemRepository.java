package practice.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.application.model.entity.OrderItemEntity;
import practice.application.model.entity.common.OrderStatus;
import practice.application.model.entity.UserEntity;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    @Query("SELECT oi FROM order_items oi " +
            "JOIN FETCH oi.order o " +
            "JOIN FETCH oi.product p " +
            "WHERE o.user = :user " +
            "AND o.orderStatus = :orderStatus")
    List<OrderItemEntity> findOrderItemsWithUserAndOrderStatus(@Param("user") UserEntity user,
                                                               @Param("orderStatus") OrderStatus orderStatus);
}

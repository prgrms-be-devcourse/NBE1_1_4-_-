package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.application.models.OrderEntity;
import practice.application.models.enumType.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    @Query("select distinct o FROM OrderEntity o join fetch o.ordersItemsList oi join fetch oi.product where o.email = :email and o.status <> :cancelStatus") // 이메일과 주문이 취소된건 조회 안함
    Optional<List<OrderEntity>> findByEmail(@Param("email") String email, @Param("cancelStatus") OrderStatus cancelStatus);
}

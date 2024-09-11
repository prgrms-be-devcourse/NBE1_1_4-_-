package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.application.models.MemberEntity;
import practice.application.models.OrderEntity;
import practice.application.models.enumType.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    @Query("SELECT distinct o FROM OrderEntity o join fetch o.ordersItemsList oi join fetch oi.product where o.member = :member and o.status = :status") // 이메일과 주문이 취소된건 조회 안함
    Optional<List<OrderEntity>> findByMemberAndStatus(@Param("member") MemberEntity member, @Param("status") OrderStatus orderStatus);

    @Query("select distinct o FROM OrderEntity o join fetch o.ordersItemsList oi join fetch oi.product where o.id = :orderId")
    Optional<OrderEntity> findFetchById(@Param("orderId") String orderId);

    @Query("SELECT o FROM OrderEntity o WHERE o.member = :member AND o.status = : RESERVED")
    Optional<OrderEntity> findByMemberAndReservedStatus(MemberEntity member);
}

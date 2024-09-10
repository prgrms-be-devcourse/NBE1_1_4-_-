package practice.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.application.models.OrdersItemEntity;

public interface OrderItemsRepository extends JpaRepository<OrdersItemEntity, Long> {

}

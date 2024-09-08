package practice.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.application.models.entities.OrderEntity;
import practice.application.models.entities.OrderItemEntity;
import practice.application.repositories.OrderItemRepository;
import practice.application.repositories.OrderRepository;

import java.util.List;
import java.util.UUID;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository     = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderEntity createOrder(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    public List<OrderItemEntity> reserveOrder(List<OrderItemEntity> itemEntities) {
        assert itemEntities != null && !itemEntities.isEmpty();

        return orderItemRepository.saveAll(itemEntities);
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderEntity getOrderById(UUID id) {
        assert id != null;

        return orderRepository.findById(id)
                              .orElse(null);
    }

    public void deleteOrderById(UUID id) {
        assert id != null;

        orderRepository.deleteById(id);
    }

}

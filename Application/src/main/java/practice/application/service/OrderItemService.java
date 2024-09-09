package practice.application.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.application.model.dto.request.OrderRequestDTO;
import practice.application.model.dto.response.OrderItemResponseDTO;
import practice.application.model.entity.OrderEntity;
import practice.application.model.entity.OrderItemEntity;
import practice.application.model.entity.ProductEntity;
import practice.application.model.entity.UserEntity;
import practice.application.model.entity.common.OrderStatus;
import practice.application.repository.OrderItemRepository;
import practice.application.service.utils.OrderItemUtils;
import practice.application.service.utils.UserUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService implements OrderItemUtils {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserUtils userUtils;

    @Transactional
    public void saveOrderItem(OrderEntity order,
                              ProductEntity product,
                              OrderRequestDTO orderRequestDTO) {
        OrderItemEntity orderItem = createOrderItem(order, product, orderRequestDTO);
        orderItemRepository.save(orderItem);
    }

    public List<OrderItemResponseDTO> findOrderItems(OrderStatus orderStatus) {
        UserEntity user = userUtils.getUserFromSecurityContext();

        List<OrderItemEntity> orderItems = orderItemRepository.findOrderItemsWithUserAndOrderStatus(user, orderStatus);
        return orderItems.stream()
                .map(orderItemEntity -> new OrderItemResponseDTO(orderItemEntity))
                .collect(Collectors.toList());
    }

    private OrderItemEntity createOrderItem(OrderEntity order,
                                            ProductEntity product,
                                            OrderRequestDTO orderRequestDTO) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrder(order);
        orderItemEntity.setProduct(product);
        orderItemEntity.setCategory(product.getCategory());
        orderItemEntity.setPrice(calPrice(orderRequestDTO.getQuantity(), product.getPrice()));
        orderItemEntity.setQuantity(orderRequestDTO.getQuantity());
        orderItemEntity.setCreated_at(LocalDateTime.now());
        orderItemEntity.setUpdated_at(LocalDateTime.now());

        return orderItemEntity;
    }

    private int calPrice(int quantity, int price) {
        return quantity * price;
    }
}

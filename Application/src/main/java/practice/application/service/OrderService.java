package practice.application.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.application.model.dto.request.OrderRequestDTO;
import practice.application.model.dto.request.PaymentRequestDTO;
import practice.application.model.entity.OrderEntity;
import practice.application.model.entity.common.OrderStatus;
import practice.application.model.entity.ProductEntity;
import practice.application.model.entity.UserEntity;
import practice.application.repository.OrderRepository;
import practice.application.service.utils.OrderItemUtils;
import practice.application.service.utils.ProductUtils;
import practice.application.service.utils.UserUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private ProductUtils productUtils;

    @Autowired
    private OrderItemUtils orderItemUtils;

    @Transactional
    public void addOrder(OrderRequestDTO orderRequestDTO) {
        UserEntity user = userUtils.getUserFromSecurityContext();
        ProductEntity product = productUtils.getProduct(orderRequestDTO.getProductId());
        OrderEntity order = findOrder(user);

        orderItemUtils.saveOrderItem(order, product, orderRequestDTO);
    }

    @Transactional
    public void updateOrder(PaymentRequestDTO paymentRequestDTO) {
        UserEntity user = userUtils.getUserFromSecurityContext();
        OrderEntity order = findOrder(user);

        order.setEmail(paymentRequestDTO.getEmail());
        order.setAddress(paymentRequestDTO.getAddress());
        order.setPostcode(paymentRequestDTO.getPostcode());
        order.setOrderStatus(OrderStatus.PAYMENT);
        order.setUpdated_at(LocalDateTime.now());
    }

    /**
     * 빈 값 Order 생성 및 저장
     * @return OrderEntity
     */
    private OrderEntity createOrder(UserEntity user) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(user);
        orderEntity.setOrderStatus(OrderStatus.NO_PAYMENT);
        orderEntity.setCreated_at(LocalDateTime.now());
        orderEntity.setUpdated_at(LocalDateTime.now());
        return orderRepository.save(orderEntity);
    }

    private OrderEntity findOrder(UserEntity user) {
        Optional<OrderEntity> optionalOrder = orderRepository.findByUserAndOrderStatus(user, OrderStatus.NO_PAYMENT);
        return optionalOrder.orElseGet(() -> createOrder(user));
    }
}

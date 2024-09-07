package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.OrderEntity;
import practice.application.models.OrdersItemEntity;
import practice.application.models.enumType.OrderStatus;
import practice.application.models.exception.NotFoundException;
import practice.application.models.exception.OrderAlreadyCancelledException;
import practice.application.repositories.OrderRepository;
import practice.application.repositories.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemsService orderItemsService;


    @Transactional
    public OrderEntity save(OrderCreateDTO orderCreateDTO) {

        List<OrdersItemEntity> orderItems = orderItemsService.createOrderItems(orderCreateDTO.getOrderItemsDTOS());

        OrderEntity orderEntity = new OrderEntity(orderCreateDTO.getEmail(), orderCreateDTO.getPostCode(), orderItems);

        return orderRepository.save(orderEntity);
    }


    public List<OrderEntity> findEmail(String email){
        List<OrderEntity> orderEntityList = orderRepository.findByEmail(email, OrderStatus.CANCEL).orElseThrow(() -> new NotFoundException("해당 이메일에 대한 주문은 없습니다"));

        if(orderEntityList.isEmpty()){
          throw new NotFoundException("해당 이메일에 대한 주문은 없습니다");
        }

        return orderEntityList;
    }








}

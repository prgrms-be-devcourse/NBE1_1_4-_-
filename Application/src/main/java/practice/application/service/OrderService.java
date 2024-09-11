package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.DTO.CommonResponseDTO;
import practice.application.models.MemberEntity;
import practice.application.models.OrderEntity;
import practice.application.models.OrdersItemEntity;
import practice.application.models.enumType.OrderStatus;
import practice.application.models.exception.NotFoundException;
import practice.application.models.exception.ImpossibleCancelException;
import practice.application.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public OrderEntity save(MemberEntity member, OrderCreateDTO orderCreateDTO) {
        List<OrdersItemEntity> orderItems = orderItemsService.createOrderItems(orderCreateDTO.getOrderItemsDTOS());

        OrderEntity orderEntity = checkExistOrder(member, orderCreateDTO, orderItems);

        return orderRepository.save(orderEntity);
    }

    @Transactional
    public CommonResponseDTO paymentOrder(String orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다"));

        orderEntity.changeStatusPayment();

        return new CommonResponseDTO("payment success");
    }
        OrderEntity orderEntity = orderRepository.findFetchById(orderId).orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다"));

        orderEntity.orderCancel();

    public OrderEntity checkExistOrder(MemberEntity member,
                                       OrderCreateDTO orderCreateDTO,
                                       List<OrdersItemEntity> orderItems) {
        Optional<OrderEntity> optionalOrder = orderRepository.findByMemberAndStatus(member, OrderStatus.RESERVED);

        if(optionalOrder.isPresent()) {
            OrderEntity orderEntity = optionalOrder.get();
            orderEntity.addOrderItems(orderItems);

            return orderEntity;
        } else {
            return new OrderEntity(member, orderCreateDTO.getEmail(), orderCreateDTO.getPostCode(), orderItems);
        }
    }
}

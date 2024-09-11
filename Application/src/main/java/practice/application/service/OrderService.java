package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.DTO.OrderResponseDTO;
import practice.application.models.DTO.PatchOrderStatusDTO;
import practice.application.models.MemberEntity;
import practice.application.models.OrderEntity;
import practice.application.models.OrdersItemEntity;
import practice.application.models.enumType.OrderStatus;
import practice.application.models.exception.NotFoundException;

import practice.application.repositories.OrderRepository;
import practice.application.models.exception.ImpossibleCancelException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemsService orderItemsService;

    public List<OrderResponseDTO> getOrders(MemberEntity member, OrderStatus orderStatus){
        List<OrderEntity> orderEntityList = orderRepository.findByMemberAndStatus(member, orderStatus)
                .orElseThrow(() -> new NotFoundException("해당 유저에 대한 주문은 없습니다"));

        if(orderEntityList.isEmpty()){
            throw new NotFoundException("해당 유저에 대한 주문은 없습니다");
        }


        return orderEntityList.stream().map(orderEntity -> new OrderResponseDTO(orderEntity)).collect(Collectors.toList());
    }

    @Transactional
    public OrderEntity save(MemberEntity member, OrderCreateDTO orderCreateDTO) {
        List<OrdersItemEntity> orderItems = orderItemsService.createOrderItems(orderCreateDTO.getOrderItemsDTOS());

        OrderEntity orderEntity = checkExistOrder(member, orderCreateDTO, orderItems);

        return orderRepository.save(orderEntity);
    }

    @Transactional
    public PatchOrderStatusDTO paymentOrder(String orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다"));

        orderEntity.changeStatusPayment();

        return new PatchOrderStatusDTO(orderEntity.getStatus());
    }

    @Transactional
    public PatchOrderStatusDTO cancelOrder(String orderId) {
        OrderEntity orderEntity = orderRepository.findFetchById(orderId).orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다"));

        if(orderEntity.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new ImpossibleCancelException();
        }

        orderEntity.orderCancel();

        return new PatchOrderStatusDTO(orderEntity.getStatus());
    }

    /**
     * Member와 status가 RESERVED인 Order를 조회합니다.
     * 있을 경우 해당 Order를 사용하여 추가로 상품을 주문할 수 있습니다.
     * 없을 경우 새로운 Order를 생성합니다.
     */
    public OrderEntity checkExistOrder(MemberEntity member,
                                       OrderCreateDTO orderCreateDTO,
                                       List<OrdersItemEntity> newOrderItems) {
        Optional<OrderEntity> optionalOrder = orderRepository.findByMemberAndReservedStatus(member);

        if(optionalOrder.isPresent()) {
            OrderEntity orderEntity = optionalOrder.get(); // 찾은 Order 사용
            orderEntity.addOrderItems(newOrderItems);

            return orderEntity;
        } else {
            // 새로운 Order 생성
            return new OrderEntity(member, orderCreateDTO.getEmail(), orderCreateDTO.getPostCode(), newOrderItems);
        }
    }
}

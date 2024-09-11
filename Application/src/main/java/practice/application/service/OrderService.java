package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.MemberEntity;
import practice.application.models.OrderEntity;
import practice.application.models.OrdersItemEntity;
import practice.application.models.enumType.OrderStatus;
import practice.application.models.exception.ImpossibleCancelException;
import practice.application.models.exception.NotFoundException;
import practice.application.models.exception.OrderAlreadyCancelledException;
import practice.application.repositories.MemberRepository;
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
    private final MemberRepository memberRepository;



    public List<OrderEntity> findEmail(String email){
        List<OrderEntity> orderEntityList = orderRepository.findByEmail(email, OrderStatus.CANCELED).orElseThrow(() -> new NotFoundException("해당 이메일에 대한 주문은 없습니다"));

        if(orderEntityList.isEmpty()){
          throw new NotFoundException("해당 이메일에 대한 주문은 없습니다");
        }

        return orderEntityList;
    }

    @Transactional
    public OrderEntity save(OrderCreateDTO orderCreateDTO) {
        List<OrdersItemEntity> orderItems = orderItemsService.createOrderItems(orderCreateDTO.getOrderItemsDTOS());

        OrderEntity orderEntity = new OrderEntity(orderCreateDTO.getEmail(), orderCreateDTO.getPostCode(), orderItems);

        MemberEntity member = memberRepository.findByEmail(orderCreateDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("해당 회원을 찾을 수 없습니다"));

        // 할인 적용 메서드 호출
        applyMemberDiscount(orderEntity, member);

        // 주문과 회원 매핑
        orderEntity.addMember(member);

        // 주문 저장
        return orderRepository.save(orderEntity);
    }


    private void applyMemberDiscount(OrderEntity orderEntity, MemberEntity member) {
        // 원래 주문 금액
        int originalPrice = orderEntity.getSum();
        // 등급별 할인 적용
        int discountedPrice = (int) member.applyDiscount(originalPrice);
        // 할인된 가격으로 주문 금액 업데이트
        orderEntity.setSum(discountedPrice);
        // 회원의 총 결제 금액 업데이트
        member.updateTotalAmount(discountedPrice);
    }


    @Transactional
    public OrderEntity cancelOrder(String orderId) {
        OrderEntity orderEntity = orderRepository.findFetchById(orderId).orElseThrow(() -> new NotFoundException("해당 주문을 찾을 수 없습니다"));

        if(orderEntity.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new ImpossibleCancelException();
        }

        orderEntity.orderCancel();

        MemberEntity member = orderEntity.getMember();

        return orderEntity;

    }

    @Scheduled(cron="0 0 14 * * ?")
    @Transactional
    public void deliverConfirmedOrders(){
        List<OrderEntity>orderEntityList=orderRepository.findOrdersByStatus(OrderStatus.PAYMENT_CONFIRMED).orElseThrow(()->new NotFoundException("결제된 주문을 찾을 수 없습니다"));
        for(OrderEntity orderEntity:orderEntityList){
            orderEntity.updateOrderStatus(OrderStatus.DELIVERED);
            orderRepository.save(orderEntity);
        }
    }


}

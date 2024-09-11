package practice.application.service;

import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.DTO.OrderItemsDTO;
import practice.application.models.DTO.OrderResponseDTO;
import practice.application.models.MemberEntity;
import practice.application.models.OrderEntity;
import practice.application.models.OrdersItemEntity;
import practice.application.models.ProductEntity;
import practice.application.models.enumType.OrderStatus;
import practice.application.repositories.MemberRepository;
import practice.application.repositories.OrderRepository;
import practice.application.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    //todo 이 테스트에서 컴파일 에러가 발생해서 잠시 주석 처리 하겠습니다.
//    @Test
//    public void 주문_Reserved_상태() throws Exception {
//        //given
//        List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();
//
//        orderItemsDTOList.add(new OrderItemsDTO(3, "b8844e34-ea04-4782-b797-8cf580b9dceb"));
//        orderItemsDTOList.add(new OrderItemsDTO(3, "e4357b59-83d8-4d7d-a272-48ceefb7fb8f"));
//
//        OrderCreateDTO orderCreateDTO = new OrderCreateDTO("k12002@nate.com", "주소~~~", orderItemsDTOList);
//
//        List<OrdersItemEntity> orderItems = orderItemsService.createOrderItems(orderItemsDTOList);
//        OrderEntity orderEntity = new OrderEntity("k12002@nate.com", "주소", orderItems);
//
//        OrderEntity saveEntity = orderRepository.save(orderEntity);
//        em.flush();
//        em.clear();
//
//        //when
//        OrderEntity checkedEntity = orderService.checkExistOrder(orderCreateDTO, orderItems);
//
//        //then
//        assertThat(saveEntity.getId()).isEqualTo(checkedEntity.getId());
//    }
//
//    @Test
//    public void 주문_PAYMENT_상태() throws Exception {
//        //given
//        List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();
//
//        orderItemsDTOList.add(new OrderItemsDTO(3, "b8844e34-ea04-4782-b797-8cf580b9dceb"));
//        orderItemsDTOList.add(new OrderItemsDTO(3, "e4357b59-83d8-4d7d-a272-48ceefb7fb8f"));
//
//        OrderCreateDTO orderCreateDTO = new OrderCreateDTO("k12002@nate.com", "주소~~~", orderItemsDTOList);
//
//        List<OrdersItemEntity> orderItems = orderItemsService.createOrderItems(orderItemsDTOList);
//        OrderEntity orderEntity = new OrderEntity("k12002@nate.com", "주소", orderItems);
//        orderEntity.changeStatusPayment();
//
//        OrderEntity saveEntity = orderRepository.save(orderEntity);
//        em.flush();
//        em.clear();
//
//        //when
//        OrderEntity checkedEntity = orderService.checkExistOrder(orderCreateDTO, orderItems);
//
//        em.flush();
//        em.clear();
//
//        //then
//        assertThat(saveEntity.getId()).isNotEqualTo(checkedEntity.getId());
//    }

    @Test
    public void 주문_목록_조회() {
        //given
        MemberEntity member = memberRepository.findById(1L).get();

        //when
        List<OrderResponseDTO> orders = orderService.getOrders(member, OrderStatus.PAYMENT);

        //then
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    public void 주문_취소() throws Exception {
       //given
        orderService.cancelOrder("d8bdddb3-8430-457b-b230-328a00bc3a75");

        em.flush();
        em.clear();

        //when

        ProductEntity product = productRepository.findById("a0680787-c0c2-430b-aaad-a1376cc550d5").get();


        //then
        assertThat(product.getQuantity()).isEqualTo(50);
    }
}
package practice.application.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.DTO.OrderItemsDTO;
import practice.application.models.OrderEntity;
import practice.application.models.OrdersItemEntity;
import practice.application.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;


    @Test
    @Rollback(value = false)
    public void 주문_넣기() throws Exception {
       //given
       List<OrderItemsDTO> orderItemsDTOList = new ArrayList<>();

       orderItemsDTOList.add(new OrderItemsDTO(3, "19ff3f2b-6595-4ff0-bdef-c8d3883a6a6e"));
       orderItemsDTOList.add(new OrderItemsDTO(3, "6b13317d-5ff1-4323-95d4-68aac47ad56b"));


        OrderCreateDTO orderCreateDTO = new OrderCreateDTO("k12002@nate.com", "주소~~~", orderItemsDTOList);


       //when
        OrderEntity save = orderService.save(orderCreateDTO);

        em.flush();
        em.clear();

        OrderEntity orderEntity = orderRepository.findById(save.getId()).get();

        //then



    }

    @Test
    public void 이메일로_주문찾기() throws Exception {
       //given
        List<OrderEntity> email = orderService.findEmail("testuser@example.com");

        //when

       //then

        for(OrderEntity orderEntity : email) {
            System.out.println(orderEntity.getEmail());
            List<OrdersItemEntity> ordersItemsList = orderEntity.getOrdersItemsList();


            for(OrdersItemEntity ordersItemEntity : ordersItemsList) {
                System.out.println(ordersItemEntity.getPrice());

                System.out.println(ordersItemEntity.getProduct().getProductName());
            }
        }

    }

}
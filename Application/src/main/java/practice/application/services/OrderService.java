package practice.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import practice.application.models.OrderStatus;
import practice.application.models.dto.OrderDTO;
import practice.application.models.dto.OrderItemDTO;
import practice.application.models.entity.OrderEntity;
import practice.application.models.entity.OrderItemEntity;
import practice.application.models.entity.ProductEntity;
import practice.application.repositories.OrderItemRepository;
import practice.application.repositories.OrderRepository;
import practice.application.repositories.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
//    상품 가격 불러올때 주로 사용할 예정
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

//    주문 생성
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        //        주문상태를 ORDER로 설정하고 Entity로 변환 후 DB에 저장
        orderDTO.setOrderStatus(OrderStatus.ORDER);
        OrderEntity orderEntity=orderDTO.toEntity();
        OrderEntity savedOrder=orderRepository.save(orderEntity);

        //        자동 생성된 주문Id 가져와서 orderDTO와 orderItemDTO 객체에 id정보 저장
        UUID orderId = savedOrder.getOrderId();
        orderDTO.setOrderId(orderId);

        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            orderItemDTO.setOrderId(orderId);
            //          productId로 해당 상품 불러오기
            ProductEntity productEntity=productRepository.findById(orderItemDTO.getProductId()).orElseThrow(()->new RuntimeException("Product not found"));
            //           상품 가격 저장
            long productPrice=productEntity.getPrice();
            //           총비용 계산해서 저장
            orderItemDTO.setPrice(orderItemDTO.getQuantity()*productPrice);
            //            상품 카테고리 저장
            orderItemDTO.setCategory(productEntity.getCategory());

            OrderItemEntity orderItemEntity=orderItemRepository.save(orderItemDTO.toOrderItemEntity());
            //            엔터티 저장하면서 자동생성된 값들을 orderItemDTO객체에도 set하기
            orderItemDTO.setSeq(orderItemEntity.getSeq());
            orderItemDTO.setCreatedAt(orderItemEntity.getCreatedAt());
            orderItemDTO.setUpdatedAt(orderItemEntity.getUpdatedAt());
        }

        //        디비에 생성, 수정된 시각 가져와서 orderDTO 객체에도 저장
        orderDTO.setCreatedAt(savedOrder.getCreatedAt());
        orderDTO.setUpdatedAt(savedOrder.getUpdatedAt());
        return orderDTO;
    }

    //    전체 주문 내역 조회
    @Transactional
    public List<OrderDTO> getAllOrderList() {
        List<OrderEntity>orderEntities=orderRepository.findAll();
        List<OrderDTO> orderDTOS=new ArrayList<>();
        for(OrderEntity orderEntity:orderEntities) {
            System.out.println("orderEntity = " + orderEntity);
            orderDTOS.add(new OrderDTO(orderEntity));
        }
        return orderDTOS;
    }


    //    주문 이메일로 주문 내역 조회
    @Transactional
    public  List<OrderDTO> getOrderByEmail(String email) {
        List<OrderEntity> orderEntities=orderRepository.findByEmail(email);
        List<OrderDTO> orderDTOS=new ArrayList<>();
        for(OrderEntity orderEntity:orderEntities) {
            orderDTOS.add(new OrderDTO(orderEntity));
        }
        return orderDTOS;
    }

}

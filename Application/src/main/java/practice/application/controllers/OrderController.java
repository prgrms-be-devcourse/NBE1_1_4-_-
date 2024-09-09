package practice.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.application.models.dto.OrderDTO;
import practice.application.models.dto.OrderItemDTO;
import practice.application.services.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

//    결제를 성공한 결과만 서버로 전달된다고 가정
//    주문 생성(OrderDTO와 OrderItemDTO 리스트를 같이 받아오기)
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder=orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);  // 201 Created 응답
    }

    //  사용자 이메일로 모든 상태의 주문 내역 전체 조회
    @GetMapping("/{email}")
    public List<OrderDTO> getAllOrders(@PathVariable String email) {
    return orderService.getOrderByEmail(email);
    }

    //    사용자 이메일로 취소된 주문내역만 조회
    @GetMapping("/{email}/canceled")
    public List<OrderDTO> getCanceledOrder(@PathVariable String email) {
        return orderService.getCanceledOrderByEmail(email);
    }

    //    사용자 이메일로 배송처리된 주문내역만 조회
    @GetMapping("/{email}/delivered")
    public List<OrderDTO> getDeliveredOrder(@PathVariable String email) {
        return orderService.getDeliveredOrderByEmail(email);
    }

    //    사용자 이메일로 주문처리된 주문 내역만 조회
    @GetMapping("/{email}/ordered")
    public List<OrderDTO> getOrderedOrder(@PathVariable String email) {
        return orderService.getOrderedOrderByEmail(email);
    }

    //   주문 아이디로 하나의 주문에 대해 어떤 주문상품 있는지 조회
    @GetMapping("/{orderId}/items")
    public List<OrderItemDTO> getOrderItemById(@PathVariable UUID orderId) {
        return orderService.getOrderItemsById(orderId);
    }

    //    주문 아이디로 주문 취소(주문 상태를 CANCELED로 변경)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrderById(@PathVariable UUID orderId) {
        orderService.cancelOrderById(orderId);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }

}

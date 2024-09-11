package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import practice.application.models.DTO.*;
import practice.application.models.Jwt.CustomUserDetails;
import practice.application.models.OrderEntity;
import practice.application.models.enumType.OrderStatus;
import practice.application.repositories.OrderRepository;
import practice.application.service.OrderService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    /**
     * 어떠한 주문 상태를 가진 주문들을 조회할 것인지 param으로 주문 상태를 받습니다.
     * 주문 목록 조회 API
     */
    @GetMapping
    public List<OrderResponseDTO> getOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestParam OrderStatus orderStatus) {
        return orderService.getOrders(customUserDetails.getMember(), orderStatus);
    }

    /**
     * 주문을 새로 생성할 수도 있고, OrderStatus가 Reserved인 주문은 추가로 상품 주문이 가능합니다.
     * 주문 생성 API
     */
    @PostMapping
    public OrderCreateResponseDTO orderCreate(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              @RequestBody OrderCreateDTO orderCreateDTO){
        OrderEntity save = orderService.save(customUserDetails.getMember(), orderCreateDTO);

        return new OrderCreateResponseDTO(save);
    }

    /**
     * 주문의 상태를 PAYMENT로 변경한다.
     * 결제 API
     */
    @PatchMapping("/payment/{orderId}")
    public PatchOrderStatusDTO payment(@PathVariable("orderId") String orderId) {
        return orderService.paymentOrder(orderId);
    }


    /**
     * 주문의 상태를 CANCELED로 변경한다.
     * 주문 취소 API
     */
    @PatchMapping("/cancel/{orderId}")
    public PatchOrderStatusDTO patchStatus(@PathVariable("orderId") String orderId){
        return orderService.cancelOrder(orderId);
    }
}

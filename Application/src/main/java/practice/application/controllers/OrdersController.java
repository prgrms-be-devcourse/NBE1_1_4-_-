package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.DTO.OrderCreateResponseDTO;
import practice.application.models.DTO.OrderResponseDTO;
import practice.application.models.DTO.PatchOrderStatus;
import practice.application.models.OrderEntity;
import practice.application.models.exception.NotFoundException;
import practice.application.repositories.OrderRepository;
import practice.application.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;


    @GetMapping("/{email}") //email로 조회하는 내가 한 주문 api
    public List<OrderResponseDTO> getOrder(@PathVariable String email) {
        List<OrderEntity> findOrderEntity = orderService.findEmail(email);

        List<OrderResponseDTO> orderResponseDTOS = new ArrayList<>();

        for(OrderEntity orderEntity : findOrderEntity) {
            orderResponseDTOS.add(new OrderResponseDTO(orderEntity));
        }

        return orderResponseDTOS;

    }


    @PostMapping("")  //주문 생성 api
    public OrderCreateResponseDTO orderCreate(@RequestBody OrderCreateDTO orderCreateDTO){
        OrderEntity save = orderService.save(orderCreateDTO);

        return new OrderCreateResponseDTO(save);
    }


    @PatchMapping("/{orderId}")
    public PatchOrderStatus patchStatus(@PathVariable("orderId") String orderId){

        OrderEntity orderEntity = orderService.cancelOrder(orderId);

        return new PatchOrderStatus(orderEntity.getStatus());   
    }











}

package practice.application.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practice.application.models.DTO.OrderCreateDTO;
import practice.application.models.DTO.OrderCreateResponseDTO;
import practice.application.models.OrderEntity;
import practice.application.service.OrderService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;


    @PostMapping("")
    public OrderCreateResponseDTO orderCreate(@RequestBody OrderCreateDTO orderCreateDTO){
        OrderEntity save = orderService.save(orderCreateDTO);

        return new OrderCreateResponseDTO(save);
    }



}

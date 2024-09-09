package practice.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import practice.application.model.dto.response.OrderItemResponseDTO;
import practice.application.model.entity.common.OrderStatus;
import practice.application.service.OrderItemService;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity getOrderItems(@RequestParam("order-status") OrderStatus orderStatus) {
        try {
            List<OrderItemResponseDTO> orderItems = orderItemService.findOrderItems(orderStatus);
            return ResponseEntity.ok(orderItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

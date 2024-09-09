package practice.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.application.model.dto.request.OrderRequestDTO;
import practice.application.model.dto.request.PaymentRequestDTO;
import practice.application.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            orderService.addOrder(orderRequestDTO);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("bad request");
        }
    }

    @PatchMapping("/payment")
    public ResponseEntity payment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            orderService.updateOrder(paymentRequestDTO);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("bad request");
        }
    }
}

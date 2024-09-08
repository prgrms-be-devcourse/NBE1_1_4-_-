package practice.application.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.application.models.dto.*;
import practice.application.models.entities.OrderEntity;
import practice.application.models.entities.OrderItemEntity;
import practice.application.models.entities.ProductEntity;
import practice.application.services.OrderService;
import practice.application.services.ProductService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/order")
public class OrderController {

    //<editor-fold desc="Inits">
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService   = orderService;
        this.productService = productService;
    }

    private final Logger logger = Logger.getLogger(OrderController.class.getName());
    //</editor-fold>

    /**
     * <li>배송 예약할 때 반드시 필요한 {@link OrderItemDTO} {@code Fields} :
     * <pre class="code">
     *      long id = [ null | empty ]
     *      String email;
     *      String address;
     *      String postcode;
     *      OrderStatus orderStatus = [ null | empty ]
     *      List< orderItemDTOs > = [null | empty]
     *  </pre>
     *
     * @param orderDTO
     * @return {@link ResponseEntity}
     */
    @Transactional
    @PostMapping("/purchase")
    public ResponseEntity<?> reserveOrder(
            @RequestBody OrderDTO orderDTO
    ) {
        UUID orderId = orderDTO.getOrderId();

        if (orderId != null) {
            logger.warning("[reserveOrder] - Given Order id is not null. Silently set id as null.");
            orderDTO.setOrderId(null);
        }

        OrderEntity orderEntity = orderService.createOrder(orderDTO.setOrderStatus(OrderStatus.PACKAGING)
                                                                   .toEntity());

        List<OrderItemEntity> itemEntities = validateOrderItemListToEntity(orderEntity, orderDTO.getOrderItemDTOs());

        if (itemEntities == null || itemEntities.isEmpty())
            return ResponseEntity.badRequest()
                                 .body("Invalid Order Items were given");

        List<OrderItemDTO> orderedItems = orderService.reserveOrder(itemEntities)
                                                      .stream()
                                                      .map(OrderItemEntity::toDTO)
                                                      .map(dto -> dto.setOrderDTO(null))
                                                      .toList();

        return ResponseEntity.ok(orderEntity.toDTO()
                                            .setOrderItemDTOs(orderedItems));
    }

    private List<OrderItemEntity> validateOrderItemListToEntity(
            OrderEntity orderEntity, List<OrderItemDTO> orderItemDTOList
    ) {

        if (orderEntity == null || orderItemDTOList == null || orderItemDTOList.isEmpty())
            return null;

        List<OrderItemEntity> validatedList = new ArrayList<>(orderItemDTOList.size());

        for (OrderItemDTO itemDTO : orderItemDTOList) {
            int quantity = itemDTO.getQuantity();
            OrderItemCategory category = itemDTO.getCategory();

            if (quantity <= 0 || category == null)
                return null;

            ProductDTO productDTO = itemDTO.getProductDTO();
            UUID productId;

            if (productDTO == null || (productId = productDTO.getProductId()) == null)
                return null;

            ProductEntity product = productService.getProductById(productId);
            if (product == null)
                return null;

            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setQuantity(quantity);
            orderItemEntity.setCategory(category.toString());
            orderItemEntity.setProductEntity(product);
            orderItemEntity.setOrderEntity(orderEntity);
            orderItemEntity.setPrice(quantity * product.getPrice());
            orderItemEntity.setCreatedAt(Instant.now());

            validatedList.add(orderItemEntity);
        }

        return validatedList;
    }

}

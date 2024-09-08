package practice.application.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
     * 주어진 주문 정보를 DB 에 저장
     *
     * <li>배송 예약할 때 반드시 필요한 {@link OrderDTO} {@code Fields} :
     * <pre class="code">
     *      long id = [ null | empty ]
     *      String email;
     *      String address;
     *      String postcode;
     *      OrderStatus orderStatus = [ null | empty ]
     *      List< orderItemDTOs > = {
     *          OrderItemCategory category;
     *          int quantity;
     *          ProductDTO productDTO = {
     *              UUID productId;
     *          }
     *      }
     *  </pre>
     *
     * @param orderDTO 주문 정보
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

    /**
     * DB 에 저장된 모든 주문 정보를 반환
     *
     * @param status  조회할 배송 상태
     * @param verbose 주문 제품 목록 포함 여부
     * @return {@link ResponseEntity}
     */
    @GetMapping("/list")
    public ResponseEntity<?> showAllOrders(
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "verbose", defaultValue = "false") boolean verbose
    ) {

        List<OrderEntity> searchResult = status == null ?
                                         orderService.getAllOrders() :
                                         orderService.getAllOrders(status);

        if (!verbose)
            return ResponseEntity.ok(searchResult.stream()
                                                 .map(OrderEntity::toDTO)
                                                 .toList());

        List<OrderDTO> verboseResult = new ArrayList<>(searchResult.size());

        for (OrderEntity orderEntity : searchResult) {
            List<OrderItemDTO> orderItems = orderEntity.getOrderItems()
                                                       .stream()
                                                       .map(OrderItemEntity::toDTO)
                                                       .map(dto -> dto.setOrderDTO(null))
                                                       .toList();

            OrderDTO orderDTO = orderEntity.toDTO()
                                           .setOrderItemDTOs(orderItems);
            verboseResult.add(orderDTO);
        }

        return ResponseEntity.ok(verboseResult);
    }

    /**
     * 특정 주문의 정보를 반환
     *
     * <li>배송 예약할 때 반드시 필요한 {@link OrderDTO} {@code Fields} :
     * <pre class="code">
     *      long id;
     * </pre>
     *
     * @param orderDTO 조회할 주문 정보 {@code ID}
     * @return {@link ResponseEntity}
     */
    @GetMapping("/detail")
    public ResponseEntity<?> showOrderDetail(@RequestBody OrderDTO orderDTO) {
        UUID id = orderDTO.getOrderId();

        if (id == null)
            return ResponseEntity.badRequest()
                                 .body("Order Id is required");

        OrderEntity orderEntity = orderService.getOrderById(id);

        if (orderEntity == null)
            return ResponseEntity.badRequest()
                                 .body("No such order with id [" + id + "] found.");

        List<OrderItemDTO> orderedItems = orderEntity.getOrderItems()
                                                     .stream()
                                                     .map(e -> e.setOrderEntity(null))
                                                     .map(OrderItemEntity::toDTO)
                                                     .toList();

        OrderDTO orderDetail = orderEntity.toDTO()
                                          .setOrderItemDTOs(orderedItems);

        return ResponseEntity.ok(orderDetail);
    }

    /**
     * 특정 주문의 제품 목록을 수정하는 메서드.
     *
     * <p>이전 연관된 모든 {@link OrderItemEntity} 를 삭제하고, 주어진 {@link OrderDTO#getOrderItemDTOs()} 로 대체함.
     *
     * <li>배송 예약할 때 반드시 필요한 {@link OrderDTO} {@code Fields} :
     * <pre class="code">
     *      long id;
     *      String email;
     *      String address;
     *      String postcode;
     *      List< orderItemDTOs > = {
     *          OrderItemCategory category;
     *          int quantity;
     *          ProductDTO productDTO = {
     *              UUID productId;
     *          }
     *      }
     *  </pre>
     *
     * @param orderDTO 수정할 주문 정보
     * @return {@link ResponseEntity}
     */
    @Transactional
    @PutMapping("/edit")
    public ResponseEntity<?> editOrder(@RequestBody OrderDTO orderDTO) {
        UUID id = orderDTO.getOrderId();

        if (id == null)
            return ResponseEntity.badRequest()
                                 .body("Order Id is required");

        OrderEntity orderEntity = orderService.getOrderById(id);

        if (orderEntity == null)
            return ResponseEntity.badRequest()
                                 .body("No such order with id [" + id + "] found.");
        else if (orderEntity.getOrderStatus()
                            .equals(OrderStatus.COMPLETED.toString()))
            return ResponseEntity.badRequest()
                                 .body("Order [" + id + "] cannot be updated due to shipping.");

        List<OrderItemEntity> newItems = validateOrderItemListToEntity(orderEntity, orderDTO.getOrderItemDTOs());

        if (newItems == null || newItems.isEmpty())
            return ResponseEntity.badRequest()
                                 .body("Invalid Order Items were given");

        List<OrderItemEntity> prevItems = new ArrayList<>(orderEntity.getOrderItems()
                                                                     .stream()
                                                                     .map(e -> e.setOrderEntity(null))
                                                                     .map(e -> e.setProductEntity(null))
                                                                     .toList());
        orderEntity.getOrderItems()
                   .clear();
        orderService.deleteOrderItems(prevItems);

        orderEntity.setEmail(orderDTO.getEmail());
        orderEntity.setAddress(orderDTO.getAddress());
        orderEntity.setPostcode(orderDTO.getPostcode());
        orderEntity.setUpdatedAt(Instant.now());
        orderEntity.getOrderItems()
                   .addAll(newItems);
        orderService.createOrder(orderEntity);

        List<OrderItemDTO> orderedItems = orderService.reserveOrder(newItems)
                                                      .stream()
                                                      .map(OrderItemEntity::toDTO)
                                                      .map(dto -> dto.setOrderDTO(null))
                                                      .toList();

        return ResponseEntity.ok(orderEntity.toDTO()
                                            .setOrderItemDTOs(orderedItems));
    }

    /**
     * {@link OrderItemEntity} DB 에 잘 저장하기 위한 메서드.
     *
     * <p>주어진 {@code orderItemDTOList} 가 올바른지 확인하고 {@code orderEntity} 와 연관된 새로운 {@link OrderItemEntity} 목록을 반환함.
     *
     * @param orderEntity      {@link OrderItemEntity} 와 연관될 {@link OrderEntity}
     * @param orderItemDTOList {@link OrderItemEntity} 목록으로 만들어질 {@link OrderItemDTO} 들
     * @return {@link ResponseEntity}
     */
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
    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrder(@RequestBody OrderDTO orderDTO) {
        UUID id = orderDTO.getOrderId();

        if (id == null)
            return ResponseEntity.badRequest()
                                 .body("Order Id is required");

        OrderEntity orderEntity = orderService.getOrderById(id);

        if (orderEntity == null)
            return ResponseEntity.badRequest()
                                 .body("No such order with id [" + id + "] found.");

        orderService.deleteOrderById(orderEntity.getOrderId());

        return ResponseEntity.ok(orderEntity.toDTO());
    }
}

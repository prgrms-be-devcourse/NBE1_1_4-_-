package practice.application.service.utils;

import practice.application.model.dto.request.OrderRequestDTO;
import practice.application.model.entity.OrderEntity;
import practice.application.model.entity.ProductEntity;

public interface OrderItemUtils {
    void saveOrderItem(OrderEntity order,
                              ProductEntity product,
                              OrderRequestDTO orderRequestDTO);
}

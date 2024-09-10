package practice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.application.models.DTO.OrderItemsDTO;
import practice.application.models.OrdersItemEntity;
import practice.application.models.ProductEntity;
import practice.application.models.exception.NotFoundException;
import practice.application.repositories.OrderItemsRepository;
import practice.application.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderItemsService {

    private final ProductRepository productRepository;




    public List<OrdersItemEntity> createOrderItems(List<OrderItemsDTO> orderItemsDTOList){
        List<OrdersItemEntity> ordersItemEntities = new ArrayList<>();

        for(OrderItemsDTO orderItemsDTO : orderItemsDTOList){
            ProductEntity product = productRepository.findById(orderItemsDTO.getProductId()).orElseThrow(() -> new NotFoundException("해당 제품은 없습니다"));

            ordersItemEntities.add(new OrdersItemEntity(orderItemsDTO.getQuantity(), product));
        }

        return ordersItemEntities;
    }
}

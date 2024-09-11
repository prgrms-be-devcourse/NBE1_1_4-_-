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


    @Transactional
    public List<OrdersItemEntity> createOrderItems(List<OrderItemsDTO> orderItemsDTOList){
        List<OrdersItemEntity> ordersItemEntities = new ArrayList<>();

        List<String> orderItemsIds = new ArrayList<>();

        for(OrderItemsDTO orderItemsDTO : orderItemsDTOList){
            orderItemsIds.add(orderItemsDTO.getProductId());

        }

        List<ProductEntity> productEntities = productRepository.findByProductId(orderItemsIds);

        if(productEntities.size() == 0){
            throw new NotFoundException("Product not found");
        }
        for(int i = 0; i < productEntities.size(); i++){
            ordersItemEntities.add(new OrdersItemEntity(orderItemsDTOList.get(i).getQuantity(), productEntities.get(i)));
        }


        return ordersItemEntities;
    }
}

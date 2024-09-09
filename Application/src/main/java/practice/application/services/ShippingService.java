package practice.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import practice.application.models.dto.OrderStatus;
import practice.application.models.entities.OrderEntity;
import practice.application.repositories.OrderRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ShippingService {

    private final OrderRepository orderRepository;

    @Autowired
    public ShippingService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private final Logger logger = Logger.getLogger(this.getClass()
                                                       .getName());

    @Transactional
    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul")
    public void sendAllPackages() {
        logger.info("Sending all packages");

        List<OrderEntity> unshippedOrders = orderRepository.findAllByOrderStatus(OrderStatus.PACKAGING.toString());

        if (unshippedOrders != null && !unshippedOrders.isEmpty()) {
            for (OrderEntity order : unshippedOrders)
                order.setOrderStatus(OrderStatus.COMPLETED.toString());

            orderRepository.saveAll(unshippedOrders);
            logger.info("All packages were send");
        }

        else
            logger.info("No Orders to be sent");
    }
}

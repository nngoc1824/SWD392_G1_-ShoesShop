package service;


import entites.Order;
import entites.PaymentStatus;
import org.springframework.stereotype.Service;
import repository.OrderRepository;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean updateOrderStatus(Long orderCode, PaymentStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findByOrderCode(orderCode);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setPaymentStatus(newStatus);
            orderRepository.save(order);
            return true;
        }
        return false;
    }
}

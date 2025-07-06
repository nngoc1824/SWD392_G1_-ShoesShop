package service;


import entites.Order;
import entites.PaymentStatus;
import org.springframework.stereotype.Service;
import repository.OrderRepository;

import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService() {
        this.orderRepository = orderRepository;
    }

    public Order findByOrderId(Integer orderCode) {
        return orderRepository.findByOrderId(orderCode);
    }

    public boolean updateOrderStatus(long orderCode, PaymentStatus newStatus) {
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

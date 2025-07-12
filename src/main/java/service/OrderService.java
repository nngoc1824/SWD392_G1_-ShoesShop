package service;


import dao.OrderDAO;
import entites.Order;


public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();

<<<<<<< Updated upstream
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
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
=======
    public Order getOrderById(int orderId) {
        return  orderDAO.getOrderById(orderId);
>>>>>>> Stashed changes
    }
}

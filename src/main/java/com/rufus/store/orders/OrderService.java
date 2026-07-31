package com.rufus.store.orders;

import com.rufus.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository
                .getOrderWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);

        var user = authService.getCurrentUser();
        if (!order.isPlacedBy(user)) {
            throw new OrderAccessDeniedException();
        }

        return orderMapper.toDto(order);
    }

    public OrderDto updateDeliveryStatus(Long orderId, DeliveryStatus deliveryStatus) {
        var order = orderRepository
                .findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.setDeliveryStatus(deliveryStatus);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }
}
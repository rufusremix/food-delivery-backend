package com.rufus.store.orders;

import com.rufus.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Page<OrderDto> getAllOrders(Pageable pageable) {
        var user = authService.getCurrentUser();
        return orderRepository.getOrdersByCustomer(user, pageable)
                .map(orderMapper::toDto);
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
package com.rufus.store.payments;

import com.rufus.store.orders.DeliveryStatus;
import com.rufus.store.orders.Order;
import com.rufus.store.carts.CartEmptyException;
import com.rufus.store.carts.CartNotFoundException;
import com.rufus.store.carts.CartRepository;
import com.rufus.store.orders.OrderRepository;
import com.rufus.store.orders.PaymentStatus;
import com.rufus.store.auth.AuthService;
import com.rufus.store.users.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;
    private final AddressRepository addressRepository;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var currentUser = authService.getCurrentUser();

        var cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(CartNotFoundException::new);

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var deliveryAddress = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AccessDeniedException("Invalid delivery address."));
        if (!deliveryAddress.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Invalid delivery address.");
        }

        var order = Order.fromCart(cart, currentUser);
        order.setDeliveryAddress(deliveryAddress);

        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);

            cart.clear();
            cartRepository.save(cart);

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        }
        catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    
                    if (paymentResult.getPaymentStatus() == PaymentStatus.PAID)
                        order.setDeliveryStatus(DeliveryStatus.CONFIRMED);
                    
                    orderRepository.save(order);
                });
    }
}
package com.rufus.store.carts;

import com.rufus.store.auth.AuthService;
import com.rufus.store.products.ProductNotFoundException;
import com.rufus.store.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;
    private final AuthService authService;

    public CartItemDto addToCart(Long productId, boolean replace) {
        var user = authService.getCurrentUser();
        var cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            var newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        var product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (!cart.isFromSameRestaurant(product)) {
            if (!replace) {
                throw new CartRestaurantConflictException();
            }
            cart.clear();
        }

        var addedItem = cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toDto(addedItem);
    }

    public CartDto getCart() {
        return cartMapper.toDto(getCurrentUserCart());
    }

    public CartItemDto updateItem(Long productId, Integer quantity) {
        var cart = getCurrentUserCart();

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(Long productId) {
        var cart = getCurrentUserCart();

        cart.removeItem(productId);

        cartRepository.save(cart);
    }

    public void clearCart() {
        var cart = getCurrentUserCart();

        cart.clear();

        cartRepository.save(cart);
    }

    private Cart getCurrentUserCart() {
        var user = authService.getCurrentUser();
        return cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);
    }
}
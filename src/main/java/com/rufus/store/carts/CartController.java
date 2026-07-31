package com.rufus.store.carts;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartDto getCart() {
        return cartService.getCart();
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemDto> addToCart(
            @Valid @RequestBody AddItemToCartRequest request,
            @RequestParam(defaultValue = "false") boolean replace) {
        var cartItemDto = cartService.addToCart(request.getProductId(), replace);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @PutMapping("/items/{productId}")
    public CartItemDto updateItem(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return cartService.updateItem(productId, request.getQuantity());
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable("productId") Long productId) {
        cartService.removeItem(productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();

        return ResponseEntity.noContent().build();
    }
}


package com.rufus.store.carts;

import com.rufus.store.products.ProductNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found."));
    }

    @ExceptionHandler(CartRestaurantConflictException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantConflict() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error",
                        "Your cart has items from a different restaurant. Clear the cart to add this item."));
    }

}

package com.rufus.store.controllers;

import com.rufus.store.dtos.AddItemToCartRequest;
import com.rufus.store.dtos.CartDto;
import com.rufus.store.dtos.CartItemDto;
import com.rufus.store.dtos.UpdateCartItemRequest;
import com.rufus.store.entities.Cart;
import com.rufus.store.entities.CartItem;
import com.rufus.store.mappers.CartMapper;
import com.rufus.store.repositories.CartRepository;
import com.rufus.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper mapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cart = new Cart();
        cartRepository.save(cart);
        var cartDto = mapper.toDto(cart);

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable("id") UUID cartId
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapper.toDto(cart));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable("id") UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request) {

        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();

        var targetProduct = productRepository.findById(request.getProductId()).orElse(null);
        if (targetProduct == null)
            return ResponseEntity.badRequest().build();


        var cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(targetProduct.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(targetProduct);
            cartItem.setQuantity(1);
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);
        var cartItemDto = mapper.toDto(cartItem);

        return ResponseEntity.ok(cartItemDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );
        }

        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product was not found in the cart.")
            );
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(mapper.toDto(cartItem));
    }
}

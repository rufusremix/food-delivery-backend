package com.rufus.store.controllers;

import com.rufus.store.dtos.AddItemToCartRequest;
import com.rufus.store.dtos.CartDto;
import com.rufus.store.dtos.CartItemDto;
import com.rufus.store.entities.Cart;
import com.rufus.store.entities.CartItem;
import com.rufus.store.mappers.CartMapper;
import com.rufus.store.repositories.CartRepository;
import com.rufus.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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


        var cartItem = cart.getCartItems()
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
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
        var cartItemDto = mapper.toDto(cartItem);

        return ResponseEntity.ok(cartItemDto);
    }
}

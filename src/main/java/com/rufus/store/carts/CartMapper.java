package com.rufus.store.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(expression = "java(cartItem.getTotalPrice())", target = "totalPrice")
    CartItemDto toDto(CartItem cartItem);
}

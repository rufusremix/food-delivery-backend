package com.rufus.store.mappers;

import com.rufus.store.dtos.CartDto;
import com.rufus.store.dtos.CartItemDto;
import com.rufus.store.entities.Cart;
import com.rufus.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    @Mapping(expression = "java(cartItem.getTotalPrice())", target = "totalPrice")
    CartItemDto toDto(CartItem cartItem);
}

package com.rufus.store.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rufus.store.restaurants.Restaurant;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    @Mapping(target = "deliveryFee", expression = "java(cart.getDeliveryFee())")
    @Mapping(target = "grandTotal", expression = "java(cart.getGrandTotal())")
    @Mapping(target = "restaurant", source = "restaurant")
    CartDto toDto(Cart cart);

    @Mapping(expression = "java(cartItem.getTotalPrice())", target = "totalPrice")
    CartItemDto toDto(CartItem cartItem);

    CartRestaurantDto toDto(Restaurant restaurant);
}

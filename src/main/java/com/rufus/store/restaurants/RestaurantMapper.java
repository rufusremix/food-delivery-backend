package com.rufus.store.restaurants;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantDto toDto(Restaurant restaurant);

    Restaurant toEntity(CreateRestaurantRequest request);
}

package com.rufus.store.products;

import com.rufus.store.restaurants.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(CreateProductRequest request);
    
    ProductCategoryDto categoryToProductCategoryDto(Category category);
    ProductRestaurantDto restaurantToProductRestaurantDto(Restaurant restaurant);
}

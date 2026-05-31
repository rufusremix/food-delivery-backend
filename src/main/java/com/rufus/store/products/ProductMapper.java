package com.rufus.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "restaurant.id", target = "restaurantId")
    ProductDto toDto(Product product);

    @Mapping(target = "restaurant", ignore = true)
    Product toEntity(CreateProductRequest request);
}

package com.rufus.store.mappers;

import com.rufus.store.dtos.CreateProductRequest;
import com.rufus.store.dtos.ProductDto;
import com.rufus.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    Product toEntity(CreateProductRequest request);
}

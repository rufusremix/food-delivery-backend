package com.rufus.store.products;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductCategoryDto category;
    private ProductRestaurantDto restaurant;
    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isVeg;
}

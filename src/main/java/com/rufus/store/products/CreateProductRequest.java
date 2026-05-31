package com.rufus.store.products;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
    private Long restaurantId;
    private String imageUrl;
    private Boolean isVeg;
    private Boolean isAvailable = true;

}

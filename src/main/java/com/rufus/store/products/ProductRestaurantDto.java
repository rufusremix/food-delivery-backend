package com.rufus.store.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRestaurantDto {
    private Long id;
    private String name;
    private Boolean isOpen;
    private BigDecimal deliveryFee;
}

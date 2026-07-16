package com.rufus.store.products;

import com.rufus.store.common.ErrorDto;
import com.rufus.store.restaurants.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(name = "categoryId", required = false) Byte categoryId,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "isVeg", required = false) String isVeg) {
        
        Boolean isVegValue = null;
        if (isVeg != null && !isVeg.isBlank()) {
            if (isVeg.equalsIgnoreCase("true")) 
                isVegValue = true;
             else if (isVeg.equalsIgnoreCase("false")) 
                isVegValue = false;
             else 
                return ResponseEntity.badRequest()
                        .body(new ErrorDto("Invalid value for 'isVeg'. Expected 'true' or 'false', got: '" + isVeg + "'"));
            
        }
        
        List<ProductDto> products = productRepository.findByFilters(categoryId, search, isVegValue)
                .stream()
                .map(productMapper::toDto)
                .toList();
        
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        var productDto = productMapper.toDto(product);

        if(product != null)
            return ResponseEntity.ok().body(productDto);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
        var product = productMapper.toEntity(request);
        product.setCategory(category);

        if (request.getRestaurantId() != null) {
            var restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow();
            product.setRestaurant(restaurant);
        }

        productRepository.save(product);

        return new ResponseEntity<>(productMapper.toDto(product), HttpStatus.CREATED);
    }
}

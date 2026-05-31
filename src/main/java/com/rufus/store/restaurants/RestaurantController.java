package com.rufus.store.restaurants;

import com.rufus.store.products.ProductDto;
import com.rufus.store.products.ProductMapper;
import com.rufus.store.products.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<RestaurantDto> getRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable Long id) {
        var restaurant = restaurantRepository.findById(id).orElse(null);

        if (restaurant != null)
            return ResponseEntity.ok().body(restaurantMapper.toDto(restaurant));
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<ProductDto>> getRestaurantMenu(@PathVariable Long id) {
        if (!restaurantRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var menu = productRepository.findByRestaurantId(id)
                .stream()
                .map(productMapper::toDto)
                .toList();

        return ResponseEntity.ok(menu);
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        var restaurant = restaurantMapper.toEntity(request);

        restaurantRepository.save(restaurant);

        return new ResponseEntity<>(restaurantMapper.toDto(restaurant), HttpStatus.CREATED);
    }
}

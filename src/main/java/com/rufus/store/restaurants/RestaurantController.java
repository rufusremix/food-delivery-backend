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
    public List<RestaurantDto> getRestaurants(
            @RequestParam(name = "search", required = false) String search) {
        List<Restaurant> restaurants;
        
        if (search != null && !search.isBlank())
            restaurants = restaurantRepository.findBySearchTerm(search);
         else
            restaurants = restaurantRepository.findAll();
        
        return restaurants.stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public RestaurantDto getRestaurant(@PathVariable Long id) {
        return restaurantMapper.toDto(
                restaurantRepository.findById(id).orElseThrow(RestaurantNotFoundException::new)
        );
    }

    @GetMapping("/{id}/menu")
    public List<ProductDto> getRestaurantMenu(@PathVariable Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException();
        }

        return productRepository.findByRestaurantId(id)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        var restaurant = restaurantMapper.toEntity(request);

        restaurantRepository.save(restaurant);

        return new ResponseEntity<>(restaurantMapper.toDto(restaurant), HttpStatus.CREATED);
    }
}

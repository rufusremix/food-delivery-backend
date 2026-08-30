package com.rufus.store.restaurants;

import com.rufus.store.common.ApiResponse;
import com.rufus.store.common.PaginationInfo;
import com.rufus.store.common.ResponseMeta;
import com.rufus.store.products.ProductDto;
import com.rufus.store.products.ProductMapper;
import com.rufus.store.products.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ApiResponse<List<RestaurantDto>>> getRestaurants(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "isOpen", required = false) Boolean isOpen,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Restaurant> restaurantPage = restaurantRepository.findByFilters(search, isOpen, pageable);

        List<RestaurantDto> restaurants = restaurantPage.getContent()
                .stream().map(restaurantMapper::toDto).toList();

        ResponseMeta meta = ResponseMeta.builder()
                .pagination(PaginationInfo.builder()
                        .currentPage(page)
                        .pageSize(size)
                        .totalItems(restaurantPage.getTotalElements())
                        .totalPages(restaurantPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(new ApiResponse<>(restaurants, meta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantDto>> getRestaurant(@PathVariable Long id) {
        var restaurant = restaurantRepository.findById(id)
                .orElseThrow(RestaurantNotFoundException::new);
        return ResponseEntity.ok(new ApiResponse<>(restaurantMapper.toDto(restaurant)));
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getRestaurantMenu(
            @PathVariable Long id,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        if (!restaurantRepository.existsById(id))
            throw new RestaurantNotFoundException();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductDto> productPage = productRepository.findByRestaurantId(id, pageable)
                .map(productMapper::toDto);

        ResponseMeta meta = ResponseMeta.builder()
                .pagination(PaginationInfo.builder()
                        .currentPage(page)
                        .pageSize(size)
                        .totalItems(productPage.getTotalElements())
                        .totalPages(productPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(new ApiResponse<>(productPage.getContent(), meta));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RestaurantDto>> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request) {

        var restaurant = restaurantMapper.toEntity(request);
        restaurantRepository.save(restaurant);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(restaurantMapper.toDto(restaurant)));
    }
}


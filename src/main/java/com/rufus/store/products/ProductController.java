package com.rufus.store.products;

import com.rufus.store.common.ApiResponse;
import com.rufus.store.common.PaginationInfo;
import com.rufus.store.common.ResponseMeta;
import com.rufus.store.restaurants.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
            @RequestParam(name = "isVeg", required = false) Boolean isVeg,
            @RequestParam(name = "isAvailable", required = false) Boolean isAvailable,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productRepository.findByFilters(categoryId, search, isVeg, isAvailable, pageable);
        List<ProductDto> products = productPage.getContent()
                .stream().map(productMapper::toDto).toList();

        PaginationInfo pagination = PaginationInfo.builder()
                .currentPage(page)
                .pageSize(size)
                .totalItems(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();

        ResponseMeta meta = ResponseMeta.builder()
                .pagination(pagination)
                .build();

        return ResponseEntity.ok(new ApiResponse<>(products, meta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ResponseEntity.ok(new ApiResponse<>(productMapper.toDto(product)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody CreateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
        var product = productMapper.toEntity(request);
        product.setCategory(category);

        if (request.getRestaurantId() != null) {
            var restaurant = restaurantRepository.findById(request.getRestaurantId()).orElseThrow();
            product.setRestaurant(restaurant);
        }

        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(productMapper.toDto(product)));
    }
}

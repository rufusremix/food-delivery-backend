package com.rufus.store.orders;

import com.rufus.store.common.ApiResponse;
import com.rufus.store.common.PaginationInfo;
import com.rufus.store.common.ResponseMeta;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        Page<OrderDto> orderPage = orderService.getAllOrders(PageRequest.of(page - 1, size));

        ResponseMeta meta = ResponseMeta.builder()
                .pagination(PaginationInfo.builder()
                        .currentPage(page)
                        .pageSize(size)
                        .totalItems(orderPage.getTotalElements())
                        .totalPages(orderPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(new ApiResponse<>(orderPage.getContent(), meta));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(new ApiResponse<>(orderService.getOrder(orderId)));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderDto>> updateDeliveryStatus(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody UpdateDeliveryStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                orderService.updateDeliveryStatus(orderId, request.getDeliveryStatus())
        ));
    }
}
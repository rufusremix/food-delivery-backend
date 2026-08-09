package com.rufus.store.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfo {
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}

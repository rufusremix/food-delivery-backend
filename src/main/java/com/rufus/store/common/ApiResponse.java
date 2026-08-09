package com.rufus.store.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private ResponseMeta meta;

    public ApiResponse(T data) {
        this.data = data;
        this.meta = null;
    }
}

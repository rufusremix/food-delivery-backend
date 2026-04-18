package com.rufus.store.mappers;

import com.rufus.store.dtos.OrderDto;
import com.rufus.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
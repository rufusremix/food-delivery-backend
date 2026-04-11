package com.rufus.store.mappers;

import com.rufus.store.dtos.RegisterUserRequest;
import com.rufus.store.dtos.UpdateUserRequest;
import com.rufus.store.dtos.UserDto;
import com.rufus.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
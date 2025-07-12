package com.example.user.mapper;

import com.example.user.domain.User;
import com.example.user.rest.dto.UserCreateRequestDto;
import com.example.user.rest.dto.UserResponseDto;
import com.example.user.rest.dto.UserUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserCreateRequestDto userCreateRequestDto);

    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "nickname", ignore = true)
    void updateUserFromDto(UserUpdateRequestDto userUpdateRequestDto, @MappingTarget User user);
} 
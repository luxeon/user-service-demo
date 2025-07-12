package com.example.user.mapper;

import com.example.user.domain.User;
import com.example.user.rest.dto.UserCreateRequestDto;
import com.example.user.rest.dto.UserPageResponseDto;
import com.example.user.rest.dto.UserResponseDto;
import com.example.user.rest.dto.UserUpdateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

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
    void updateUserFromDto(UserUpdateRequestDto userUpdateRequestDto, @MappingTarget User user);

    @Mapping(target = "page.size", source = "size")
    @Mapping(target = "page.number", source = "number")
    @Mapping(target = "page.totalElements", source = "totalElements")
    @Mapping(target = "page.totalPages", source = "totalPages")
    @Mapping(target = "content", source = "content")
    UserPageResponseDto toUserPageResponseDto(Page<User> page);
} 
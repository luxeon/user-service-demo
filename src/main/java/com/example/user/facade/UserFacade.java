package com.example.user.facade;

import com.example.user.domain.User;
import com.example.user.mapper.UserMapper;
import com.example.user.rest.dto.UserCreateRequestDto;
import com.example.user.rest.dto.UserResponseDto;
import com.example.user.rest.dto.UserUpdateRequestDto;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        User createdUser = userService.create(userCreateRequestDto);
        return userMapper.toUserResponseDto(createdUser);
    }

    public UserResponseDto update(UUID id, UserUpdateRequestDto userUpdateRequestDto) {
        User updatedUser = userService.update(id, userUpdateRequestDto);
        return userMapper.toUserResponseDto(updatedUser);
    }

    public void delete(UUID id) {
        userService.delete(id);
    }
} 
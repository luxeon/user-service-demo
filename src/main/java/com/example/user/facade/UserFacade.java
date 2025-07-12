package com.example.user.facade;

import com.example.user.domain.User;
import com.example.user.mapper.UserMapper;
import com.example.user.rest.dto.UserCreateRequestDto;
import com.example.user.rest.dto.UserResponseDto;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        User user = userService.create(userCreateRequestDto);
        return userMapper.toUserResponseDto(user);
    }
} 
package com.example.user.rest;

import com.example.user.facade.UserFacade;
import com.example.user.rest.dto.UserCreateRequestDto;
import com.example.user.rest.dto.UserResponseDto;
import com.example.user.rest.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserFacade userFacade;

    @Override
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        return userFacade.create(userCreateRequestDto);
    }

    @Override
    public UserResponseDto update(UUID id, UserUpdateRequestDto userUpdateRequestDto) {
        return userFacade.update(id, userUpdateRequestDto);
    }
} 
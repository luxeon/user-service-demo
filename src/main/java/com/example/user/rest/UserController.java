package com.example.user.rest;

import com.example.user.rest.dto.UserCreateRequestDto;
import com.example.user.rest.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    // private final UserFacade userFacade;

    @Override
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        // return userFacade.create(userCreateRequestDto);
        throw new UnsupportedOperationException("Not implemented yet");
    }
} 
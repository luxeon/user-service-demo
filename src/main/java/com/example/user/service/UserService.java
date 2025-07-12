package com.example.user.service;

import com.example.user.domain.User;
import com.example.user.exception.UserAlreadyExistsException;
import com.example.user.exception.UserNotFoundException;
import com.example.user.mapper.UserMapper;
import com.example.user.repository.UserRepository;
import com.example.user.rest.dto.UserCreateRequestDto;
import com.example.user.rest.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User create(UserCreateRequestDto userCreateRequestDto) {
        if (userRepository.existsByEmailOrNickname(userCreateRequestDto.getEmail(), userCreateRequestDto.getNickname())) {
            throw new UserAlreadyExistsException("User with the provided email or nickname already exists");
        }
        User user = userMapper.toUser(userCreateRequestDto);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        return userRepository.save(user);
    }

    @Transactional
    public User update(UUID id, UserUpdateRequestDto userUpdateRequestDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (userRepository.existsByEmailOrNickname(userUpdateRequestDto.getEmail(), userUpdateRequestDto.getNickname())) {
            throw new UserAlreadyExistsException("User with the provided email or nickname already exists");
        }

        userMapper.updateUserFromDto(userUpdateRequestDto, user);
        return userRepository.save(user);
    }
} 
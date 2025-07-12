package com.example.user.mapper;

import com.example.user.domain.User;
import com.example.user.domain.event.UserCreatedEvent;
import com.example.user.domain.event.UserDeletedEvent;
import com.example.user.domain.event.UserUpdatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEventMapper {

    UserCreatedEvent toUserCreatedEvent(User user);

    UserUpdatedEvent toUserUpdatedEvent(User user);

    UserDeletedEvent toUserDeletedEvent(User user);
}
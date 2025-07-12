package com.example.user.domain.event;

import java.util.UUID;

public record UserCreatedEvent(UUID id, String firstName, String lastName, String nickname, String email, String country) {
} 
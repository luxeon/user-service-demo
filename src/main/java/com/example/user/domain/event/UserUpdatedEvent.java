package com.example.user.domain.event;

import java.util.UUID;

public record UserUpdatedEvent(UUID id, String firstName, String lastName, String nickname, String email, String country) {
} 
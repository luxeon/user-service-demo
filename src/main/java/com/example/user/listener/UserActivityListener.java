package com.example.user.listener;

import com.example.user.domain.event.UserCreatedEvent;
import com.example.user.domain.event.UserDeletedEvent;
import com.example.user.domain.event.UserUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class UserActivityListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Sending user created kafka message here... Event: {}", event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserUpdatedEvent(UserUpdatedEvent event) {
        log.info("Sending user updated kafka message here... Event: {}", event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserDeletedEvent(UserDeletedEvent event) {
        log.info("Sending user deleted kafka message here... Event: {}", event);
    }
} 
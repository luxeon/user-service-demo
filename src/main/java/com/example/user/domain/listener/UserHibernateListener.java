package com.example.user.domain.listener;

import com.example.user.domain.User;
import com.example.user.domain.event.UserDeletedEvent;
import com.example.user.mapper.UserEventMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHibernateListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

    private final ApplicationEventPublisher publisher;
    private final UserEventMapper userEventMapper;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (event.getEntity() instanceof User user) {
            publisher.publishEvent(userEventMapper.toUserCreatedEvent(user));
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity() instanceof User user) {
            publisher.publishEvent(userEventMapper.toUserUpdatedEvent(user));
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        if (event.getEntity() instanceof User user) {
            publisher.publishEvent(userEventMapper.toUserDeletedEvent(user));
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
} 
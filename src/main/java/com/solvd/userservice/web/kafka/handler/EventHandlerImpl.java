package com.solvd.userservice.web.kafka.handler;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventHandlerImpl implements EventHandler {

    private final UserCreateEventHandler userCreateEventHandler;
    private final UserUpdateEventHandler userUpdateEventHandler;
    private final UserDeleteEventHandler userDeleteEventHandler;
    private final UserActivateEventHandler userActivateEventHandler;
    private final UserResetPasswordEventHandler userResetPasswordEventHandler;
    private final UserUpdatePasswordEventHandler userUpdatePasswordEventHandler;

    @Override
    @KafkaListener(topics = "events")
    public void handle(final ConsumerRecord<String, Object> record,
                       final Acknowledgment acknowledgment) {
        userCreateEventHandler.handle(record, acknowledgment);
        userUpdateEventHandler.handle(record, acknowledgment);
        userDeleteEventHandler.handle(record, acknowledgment);
        userActivateEventHandler.handle(record, acknowledgment);
        userResetPasswordEventHandler.handle(record, acknowledgment);
        userUpdatePasswordEventHandler.handle(record, acknowledgment);
    }

}

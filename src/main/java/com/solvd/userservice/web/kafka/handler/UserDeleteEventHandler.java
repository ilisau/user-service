package com.solvd.userservice.web.kafka.handler;

import com.google.gson.Gson;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.UserDeleteEvent;
import com.solvd.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleteEventHandler implements EventHandler {

    private final UserRepository userRepository;
    private final Gson gson;

    @Override
    public void handle(final ConsumerRecord<String, Object> record,
                       final Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UserDeleteEvent event = gson.fromJson(json, UserDeleteEvent.class);
        if (event.getType() == EventType.USER_DELETE) {
            userRepository.deleteById(event.getAggregateId()).subscribe();
            acknowledgment.acknowledge();
        }
    }

}

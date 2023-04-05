package com.solvd.userservice.web.kafka.handler;

import com.google.gson.Gson;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.agregate.AggregateFactory;
import com.solvd.userservice.domain.agregate.UserAggregate;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.UserActivateEvent;
import com.solvd.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserActivateEventHandler implements EventHandler {

    private final UserRepository userRepository;
    private final Gson gson;

    @Override
    public void handle(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UserActivateEvent event = gson.fromJson(json, UserActivateEvent.class);
        if (event.getType() == EventType.USER_ACTIVATE) {
            Mono<User> user = userRepository.findById(event.getAggregateId());
            Mono<UserAggregate> aggregate = AggregateFactory.toAggregate(user);
            aggregate = event.copyTo(aggregate);
            aggregate.flatMap(AggregateFactory::toUser)
                    .flatMap(userRepository::save)
                    .subscribe();
            acknowledgment.acknowledge();
        }
    }

}

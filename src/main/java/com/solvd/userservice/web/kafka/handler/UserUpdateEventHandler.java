package com.solvd.userservice.web.kafka.handler;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.agregate.AggregateFactory;
import com.solvd.userservice.domain.agregate.UserAggregate;
import com.solvd.userservice.domain.event.AbstractEvent;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.UserUpdateEvent;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.web.kafka.parser.UserParser;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserUpdateEventHandler implements EventHandler {

    private final UserRepository userRepository;
    private final Gson gson;
    private final UserParser userParser;

    @Override
    public void handle(final ConsumerRecord<String, Object> record,
                       final Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UserUpdateEvent event = gson.fromJson(json, UserUpdateEvent.class);
        if (event.getType() == EventType.USER_UPDATE) {
            LinkedTreeMap<String, String> payload =
                    (LinkedTreeMap) event.getPayload();
            User user = userParser.parse(payload);
            event.setPayload(user);
            Mono<UserAggregate> aggregate = getUserAggregate(event);
            aggregate = event.copyTo(aggregate);
            aggregate.flatMap(AggregateFactory::toUser)
                    .flatMap(userRepository::save)
                    .subscribe();
            acknowledgment.acknowledge();
        }
    }

    private Mono<UserAggregate> getUserAggregate(final AbstractEvent event) {
        Mono<UserAggregate> aggregate = null;
        if (event.getAggregateId() != null) {
            Mono<User> error = Mono.error(
                    new UserNotFoundException("User with id "
                            + event.getAggregateId() + " not found"));
            Mono<User> user = userRepository.findById(event.getAggregateId())
                    .switchIfEmpty(error);
            aggregate = AggregateFactory.toAggregate(user);
        }
        return aggregate;
    }

}

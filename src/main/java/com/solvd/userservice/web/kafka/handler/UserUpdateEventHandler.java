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

    @Override
    public void handle(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UserUpdateEvent event = gson.fromJson(json, UserUpdateEvent.class);
        if (event.getType() == EventType.USER_UPDATE) {
            LinkedTreeMap<String, String> payload = (LinkedTreeMap) event.getPayload();
            User user = parseUser(payload);
            event.setPayload(user);
            Mono<UserAggregate> aggregate = getUserAggregate(event);
            aggregate = event.copyTo(aggregate);
            aggregate.flatMap(AggregateFactory::toUser)
                    .flatMap(userRepository::save)
                    .subscribe();
            acknowledgment.acknowledge();
        }
    }

    private User parseUser(LinkedTreeMap<String, String> linkedTreeMap) {
        User user = new User();
        user.setId(linkedTreeMap.get("id"));
        user.setName(linkedTreeMap.get("name"));
        user.setSurname(linkedTreeMap.get("surname"));
        user.setEmail(linkedTreeMap.get("email"));
        if (linkedTreeMap.get("role") != null) {
            user.setRole(User.Role.valueOf(linkedTreeMap.get("role")));
        }
        user.setPassword(linkedTreeMap.get("password"));
        user.setActivated(Boolean.getBoolean(linkedTreeMap.get("isActivated")));
        return user;
    }

    private Mono<UserAggregate> getUserAggregate(AbstractEvent event) {
        Mono<UserAggregate> aggregate = null;
        if (event.getAggregateId() != null) {
            Mono<User> error = Mono.error(new UserNotFoundException("User with id " + event.getAggregateId() + " not found"));
            Mono<User> user = userRepository.findById(event.getAggregateId())
                    .switchIfEmpty(error);
            aggregate = AggregateFactory.toAggregate(user);
        }
        return aggregate;
    }

}

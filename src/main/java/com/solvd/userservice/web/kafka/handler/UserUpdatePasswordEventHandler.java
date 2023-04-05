package com.solvd.userservice.web.kafka.handler;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.agregate.AggregateFactory;
import com.solvd.userservice.domain.agregate.UserAggregate;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.UpdatePasswordEvent;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.web.kafka.parser.PasswordParser;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserUpdatePasswordEventHandler implements EventHandler {

    private final UserRepository userRepository;
    private final Gson gson;
    private final PasswordParser passwordParser;

    @Override
    public void handle(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UpdatePasswordEvent event = gson.fromJson(json, UpdatePasswordEvent.class);
        if (event.getType() == EventType.UPDATE_PASSWORD) {
            LinkedTreeMap<String, String> payload = (LinkedTreeMap) event.getPayload();
            Password password = passwordParser.parse(payload);
            event.setPayload(password);
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

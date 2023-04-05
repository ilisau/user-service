package com.solvd.userservice.web.kafka.handler;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.UserCreateEvent;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.MailService;
import com.solvd.userservice.web.kafka.parser.UserParser;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreateEventHandler implements EventHandler {

    private final UserRepository userRepository;
    private final Gson gson;
    private final UserParser userParser;
    private final MailService mailService;

    @Override
    public void handle(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UserCreateEvent event = gson.fromJson(json, UserCreateEvent.class);
        if (event.getType() == EventType.USER_CREATE) {
            LinkedTreeMap<String, String> payload = (LinkedTreeMap) event.getPayload();
            User user = userParser.parse(payload);
            userRepository.save(user).subscribe();
            mailService.sendActivationMail(user);
            acknowledgment.acknowledge();
        }
    }

}

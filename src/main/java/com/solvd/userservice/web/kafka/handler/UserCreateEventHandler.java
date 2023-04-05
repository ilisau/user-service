package com.solvd.userservice.web.kafka.handler;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.solvd.userservice.domain.MailData;
import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.UserCreateEvent;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.JwtService;
import com.solvd.userservice.web.kafka.MessageSender;
import com.solvd.userservice.web.mapper.MailDataMapper;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCreateEventHandler implements EventHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MessageSender messageSender;
    private final MailDataMapper mailDataMapper;
    private final Gson gson;

    @Override
    public void handle(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UserCreateEvent event = gson.fromJson(json, UserCreateEvent.class);
        if (event.getType() == EventType.USER_CREATE) {
            LinkedTreeMap<String, String> payload = (LinkedTreeMap) event.getPayload();
            User user = parseUser(payload);
            userRepository.save(user).subscribe();
            Map<String, Object> params = new HashMap<>();
            String token = jwtService.generateToken(JwtTokenType.ACTIVATION, user);
            params.put("token", token);
            params.put("user.name", user.getName());
            params.put("user.surname", user.getSurname());
            params.put("user.email", user.getEmail());
            params.put("user.id", user.getId());
            messageSender.sendMessage("mail",
                    0,
                    String.valueOf(user.hashCode()),
                    mailDataMapper.toDto(new MailData(MailType.ACTIVATION, params))).subscribe();
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

}

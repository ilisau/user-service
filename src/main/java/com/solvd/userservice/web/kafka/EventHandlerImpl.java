package com.solvd.userservice.web.kafka;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.solvd.userservice.domain.MailData;
import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.agregate.AggregateFactory;
import com.solvd.userservice.domain.agregate.UserAggregate;
import com.solvd.userservice.domain.event.AbstractEvent;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.ResetPasswordEvent;
import com.solvd.userservice.domain.event.UpdatePasswordEvent;
import com.solvd.userservice.domain.event.UserActivateEvent;
import com.solvd.userservice.domain.event.UserCreateEvent;
import com.solvd.userservice.domain.event.UserDeleteEvent;
import com.solvd.userservice.domain.event.UserUpdateEvent;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.JwtService;
import com.solvd.userservice.web.mapper.MailDataMapper;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventHandlerImpl implements EventHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MessageSender messageSender;
    private final MailDataMapper mailDataMapper;
    private final Gson gson;

    @Override
    @KafkaListener(topics = "events")
    public void handle(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        userCreateEvent(record, acknowledgment);
        userUpdateEvent(record, acknowledgment);
        userDeleteEvent(record, acknowledgment);
        userActivateEvent(record, acknowledgment);
        userResetPasswordEvent(record, acknowledgment);
        userUpdatePasswordEvent(record, acknowledgment);
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

    private Password parsePassword(LinkedTreeMap<String, String> linkedTreeMap) {
        Password password = new Password();
        password.setNewPassword(linkedTreeMap.get("newPassword"));
        password.setOldPassword(linkedTreeMap.get("oldPassword"));
        return password;
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

    private void userCreateEvent(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
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

    private void userUpdateEvent(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
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

    private void userDeleteEvent(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UserDeleteEvent event = gson.fromJson(json, UserDeleteEvent.class);
        if (event.getType() == EventType.USER_DELETE) {
            userRepository.deleteById(event.getAggregateId()).subscribe();
            acknowledgment.acknowledge();
        }
    }

    private void userActivateEvent(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
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

    private void userResetPasswordEvent(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        ResetPasswordEvent event = gson.fromJson(json, ResetPasswordEvent.class);
        if (event.getType() == EventType.RESET_PASSWORD) {
            LinkedTreeMap<String, String> payload = (LinkedTreeMap) event.getPayload();
            Password password = parsePassword(payload);
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

    private void userUpdatePasswordEvent(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment) {
        String json = (String) record.value();
        UpdatePasswordEvent event = gson.fromJson(json, UpdatePasswordEvent.class);
        if (event.getType() == EventType.UPDATE_PASSWORD) {
            LinkedTreeMap<String, String> payload = (LinkedTreeMap) event.getPayload();
            Password password = parsePassword(payload);
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

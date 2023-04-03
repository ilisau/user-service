package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.event.AbstractEvent;
import com.solvd.userservice.domain.event.EventType;
import com.solvd.userservice.domain.event.ResetPasswordEvent;
import com.solvd.userservice.domain.event.UpdatePasswordEvent;
import com.solvd.userservice.domain.event.UserActivateEvent;
import com.solvd.userservice.domain.event.UserCreateEvent;
import com.solvd.userservice.domain.event.UserDeleteEvent;
import com.solvd.userservice.domain.event.UserUpdateEvent;
import com.solvd.userservice.domain.exception.InvalidTokenException;
import com.solvd.userservice.domain.exception.PasswordMismatchException;
import com.solvd.userservice.domain.exception.UserAlreadyExistsException;
import com.solvd.userservice.domain.jwt.JwtToken;
import com.solvd.userservice.repository.EventRepository;
import com.solvd.userservice.service.JwtService;
import com.solvd.userservice.service.UserEventService;
import com.solvd.userservice.service.UserQueryService;
import com.solvd.userservice.web.kafka.MessageSender;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserEventServiceImpl implements UserEventService {

    private final JwtService jwtService;
    private final MessageSender messageSender;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserQueryService userQueryService;

    @Override
    @CacheEvict(value = "users", key = "#user.id")
    public Mono<Void> update(User user) {
        return checkIfEmailIsAvailable(user)
                .onErrorResume(Mono::error)
                .flatMap(u -> {
                    AbstractEvent event = new UserUpdateEvent();
                    event.setType(EventType.USER_UPDATE);
                    event.setPayload(user);
                    event.setAggregateId(user.getId());
                    eventRepository.save(event);
                    return messageSender.sendMessage("events", 0, String.valueOf(user.hashCode()), event)
                            .then();
                });
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public Mono<Void> updatePassword(String userId, String newPassword) {
        AbstractEvent event = new ResetPasswordEvent();
        event.setAggregateId(userId);
        event.setPayload(passwordEncoder.encode(newPassword));
        event.setType(EventType.RESET_PASSWORD);
        eventRepository.save(event);
        return messageSender.sendMessage("events", 0, userId, event)
                .then();
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public Mono<Void> updatePassword(String userId, Password password) {
        Mono<User> user = userQueryService.getById(userId);
        return user.flatMap(u -> {
                    if (!passwordEncoder.matches(password.getOldPassword(), u.getPassword())) {
                        return Mono.error(new PasswordMismatchException("old password is incorrect"));
                    }
                    password.setNewPassword(passwordEncoder.encode(password.getNewPassword()));
                    return user;
                })
                .flatMap(u -> {
                    AbstractEvent event = new UpdatePasswordEvent();
                    event.setAggregateId(userId);
                    event.setPayload(password);
                    event.setType(EventType.UPDATE_PASSWORD);
                    eventRepository.save(event);
                    return messageSender.sendMessage("events", 0, userId, event)
                            .then();
                });
    }

    @Override
    public Mono<Void> create(User user) {
        return checkIfEmailIsAvailable(user)
                .onErrorResume(Mono::error)
                .map(value -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setActivated(false);
                    return user;
                })
                .flatMap(u -> {
                    AbstractEvent event = new UserCreateEvent();
                    event.setType(EventType.USER_CREATE);
                    event.setPayload(u);
                    eventRepository.save(event);
                    return messageSender.sendMessage("events", 0, String.valueOf(user.hashCode()), event)
                            .then();
                });
    }

    private Mono<Boolean> checkIfEmailIsAvailable(User user) {
        Mono<User> userWithSameEmail = userQueryService.getByEmail(user.getEmail());
        Mono<Boolean> error = Mono.error(new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists"));
        return userWithSameEmail
                .onErrorResume(u -> Mono.just(user))
                .flatMap(u -> {
                    if (!u.equals(user) && !Objects.equals(u.getId(), user.getId())) {
                        return error;
                    }
                    return Mono.just(true);
                });
    }

    @Override
    public Mono<Void> activate(JwtToken token) {
        if (!jwtService.validateToken(token.getToken())) {
            throw new InvalidTokenException("token is expired");
        }
        if (!jwtService.isTokenType(token.getToken(), JwtTokenType.ACTIVATION)) {
            throw new InvalidTokenException("invalid reset token");
        }
        AbstractEvent event = new UserActivateEvent();
        event.setAggregateId(jwtService.retrieveUserId(token.getToken()));
        event.setType(EventType.USER_ACTIVATE);
        eventRepository.save(event);
        return messageSender.sendMessage("events", 0, String.valueOf(token.hashCode()), event)
                .then();
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public Mono<Void> delete(String id) {
        AbstractEvent event = new UserDeleteEvent();
        event.setType(EventType.USER_DELETE);
        event.setAggregateId(id);
        eventRepository.save(event);
        return messageSender.sendMessage("events", 0, id, event)
                .then();
    }

}

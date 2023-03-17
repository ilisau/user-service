package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.MailData;
import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.exception.InvalidTokenException;
import com.solvd.userservice.domain.exception.PasswordMismatchException;
import com.solvd.userservice.domain.exception.UserAlreadyExistsException;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import com.solvd.userservice.domain.jwt.JwtToken;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.JwtService;
import com.solvd.userservice.service.UserService;
import com.solvd.userservice.web.kafka.MessageSender;
import com.solvd.userservice.web.mapper.MailDataMapper;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MessageSender messageSender;
    private final MailDataMapper mailDataMapper;

    @Override
    public Mono<User> getById(Long id) {
        Mono<User> error = Mono.error(new UserNotFoundException("User with id " + id + " not found"));
        return userRepository.findById(id)
                .switchIfEmpty(error);
    }

    @Override
    public Mono<User> getByEmail(String email) {
        Mono<User> error = Mono.error(new UserNotFoundException("User with email " + email + " not found"));
        return userRepository.findByEmail(email)
                .switchIfEmpty(error);
    }

    @Override
    public Mono<User> update(User user) {
        return checkIfEmailIsAvailable(user)
                .onErrorResume(Mono::error)
                .map(value -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user).subscribe();
                    return user;
                });
    }

    private Mono<Boolean> checkIfEmailIsAvailable(User user) {
        Mono<User> userWithSameEmail = userRepository.findByEmail(user.getEmail())
                .switchIfEmpty(Mono.just(user));
        Mono<Boolean> error = Mono.error(new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists"));
        return userWithSameEmail
                .flatMap(u -> {
                    if (!u.equals(user) && !Objects.equals(u.getId(), user.getId())) {
                        return error;
                    }
                    return Mono.just(true);
                });
    }

    @Override
    public Mono<Void> updatePassword(Long userId, String newPassword) {
        Mono<User> user = getById(userId);
        return user.map(u -> {
                    u.setPassword(passwordEncoder.encode(newPassword));
                    return u;
                })
                .flatMap(userRepository::save)
                .then();
    }

    @Override
    public Mono<Void> updatePassword(Long userId, Password password) {
        Mono<User> user = getById(userId);
        return user.flatMap(u -> {
                    if (!passwordEncoder.matches(password.getOldPassword(), u.getPassword())) {
                        return Mono.error(new PasswordMismatchException("old password is incorrect"));
                    }
                    return user;
                })
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(password.getNewPassword()));
                    return u;
                })
                .flatMap(userRepository::save)
                .then();
    }

    @Override
    public Mono<User> create(User user) {
        return checkIfEmailIsAvailable(user)
                .onErrorResume(Mono::error)
                .map(value -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setActivated(false);
                    return user;
                })
                .flatMap(userRepository::save)
                .map(u -> {
                    Map<String, Object> params = new HashMap<>();
                    String token = jwtService.generateToken(JwtTokenType.ACTIVATION, user);
                    params.put("token", token);
                    params.put("user.name", user.getName());
                    params.put("user.surname", user.getSurname());
                    params.put("user.email", user.getEmail());
                    params.put("user.id", user.getId());
                    return params;
                })
                .flatMap(params -> messageSender.sendMessage("mail",
                                0,
                                params.get("user.id").toString(),
                                mailDataMapper.toDto(new MailData(MailType.ACTIVATION, params)))
                        .then())
                .map(u -> user);
    }

    @Override
    public Mono<Void> activate(JwtToken token) {
        if (!jwtService.validateToken(token.getToken())) {
            throw new InvalidTokenException("token is expired");
        }
        if (!jwtService.isTokenType(token.getToken(), JwtTokenType.ACTIVATION)) {
            throw new InvalidTokenException("invalid reset token");
        }
        Long id = jwtService.retrieveUserId(token.getToken());
        Mono<User> user = getById(id);
        return user.map(u -> {
                    u.setActivated(true);
                    return u;
                })
                .flatMap(userRepository::save)
                .then();
    }

    @Override
    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }

}

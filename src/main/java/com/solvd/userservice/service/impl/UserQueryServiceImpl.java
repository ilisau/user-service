package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final ReactiveHashOperations<String, String, User> hashOps;

    @Override
    public Mono<User> getById(final String id) {
        return hashOps.get("user", id)
                .switchIfEmpty(getByIdAndCache(id));
    }

    private Mono<User> getByIdAndCache(final String userId) {
        Mono<User> error = Mono.error(
                new UserNotFoundException("User with id "
                        + userId
                        + " not found")
        );
        return userRepository
                .findById(userId)
                .switchIfEmpty(error)
                .flatMap(user -> hashOps.put("user", userId, user)
                        .thenReturn(user));
    }

    @Override
    public Mono<User> getByEmail(final String email) {
        return hashOps.get("user", email)
                .switchIfEmpty(getByEmailAndCache(email));
    }

    private Mono<User> getByEmailAndCache(final String email) {
        Mono<User> error = Mono.error(
                new UserNotFoundException("User with email " + email
                        + " not found")
        );
        return userRepository
                .findByEmail(email)
                .switchIfEmpty(error)
                .flatMap(user -> hashOps.put("user", email, user)
                        .thenReturn(user));
    }

}
